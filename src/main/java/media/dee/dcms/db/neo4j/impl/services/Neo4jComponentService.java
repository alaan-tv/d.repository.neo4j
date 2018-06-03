package media.dee.dcms.db.neo4j.impl.services;

import media.dee.dcms.core.db.GraphDatabaseService;
import media.dee.dcms.core.db.NoSuchRecordException;
import media.dee.dcms.core.db.Record;
import media.dee.dcms.core.services.ComponentService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

@Component
public class Neo4jComponentService implements ComponentService {

    private GraphDatabaseService<?> graphDatabaseService;

    @Reference
    void setGraphDatabaseService(GraphDatabaseService<?> graphDatabaseService){
        this.graphDatabaseService = graphDatabaseService;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> findComponentById(long componentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", componentId);
        try {
            return (Map<String, Object>) graphDatabaseService.fetchOne("MATCH (c:Component{id: {id} }) return c", params).get("c");
        } catch (NoSuchRecordException e) {
            throw new RuntimeException(e);
        }
    }
}
