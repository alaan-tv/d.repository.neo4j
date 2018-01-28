
package media.dee.repository.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import media.dee.core.repository.api.ComponentRepository;
import media.dee.repository.internal.CypherExecutor;
import org.neo4j.driver.v1.Record;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component(immediate = true, service = ComponentRepository.class)
public class DefaultComponentRepository  implements ComponentRepository {

    private final static ResourceBundle queries = ResourceBundle.getBundle(DefaultComponentRepository.class.getName());

    private CypherExecutor queryExecutor;

    private LoadingCache<String, String> queryCache;
    private void buildCache() {
        queryCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES)
                                 .maximumSize(1000)
                                 .build(new CacheLoader<String, String>() {
                                @Override
                                public String load(String componentID) throws Exception {
                                    String qry = "MATCH(c:Component{id:{componentID}}) RETURN C.query as query";
                                     Map result = queryExecutor.fetchOne(qry, Collections.singletonMap("componentID",componentID)).asMap();
                                     return result.get("query").toString();
                                }
                            });
    }


    @Reference
    public void setCypherExecutor(CypherExecutor executor){
        this.queryExecutor = executor;
        buildCache();
    }

    @Override
    public Map<String,Object> findOne(String componentID, Map context) {
        try {
            String query = queryCache.get(componentID);
            Record record = this.queryExecutor.fetchOne(query, context);
            return record.asMap();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }

}
