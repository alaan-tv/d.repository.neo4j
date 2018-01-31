package media.dee.repository.impl;

import media.dee.core.repository.api.ComponentRepository;
import media.dee.core.repository.api.ContentRepository;
import media.dee.repository.internal.CypherExecutor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

@Component(immediate = true,service = ContentRepository.class)
public class DefaultContentRepository implements ContentRepository {

    private final static ResourceBundle queries = ResourceBundle.getBundle(DefaultContentRepository.class.getName());
    private CypherExecutor queryExecutor;

    @Override
    public Map<String, Object> findOne(String link) {
        String qry = queries.getString("Find.By.ID");
        return queryExecutor.fetchOne(qry, Collections.singletonMap("link",link)).asMap();
    }

    @Reference
    public void setCypherExecutor(CypherExecutor executor){
        this.queryExecutor = executor;
    }

}
