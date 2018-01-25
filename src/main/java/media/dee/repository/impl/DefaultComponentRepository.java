
package media.dee.repository.impl;

import media.dee.core.repository.api.ComponentRepository;
import media.dee.repository.internal.CypherExecutor;
import org.neo4j.driver.v1.Record;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

@Component(immediate = true, service = ComponentRepository.class)
public class DefaultComponentRepository  implements ComponentRepository {

    private final static ResourceBundle queries = ResourceBundle.getBundle(DefaultComponentRepository.class.getName());

    private CypherExecutor queryExecutor;

    @Reference
    public void setCypherExecutor(CypherExecutor executor){
        this.queryExecutor = executor;
    }

    @Override
    public Map<String,Object> findOne(String ComponentID, Map context) {

        Record record = this.queryExecutor.fetchOne(queries.getString("Component.By.Id"), Collections.emptyMap());
        return record.asMap();
    }

}
