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
    public Record findComponentById(long componentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", componentId);
        try {
            return graphDatabaseService.fetchOne(
                    "MATCH (template:Component{id: {id} })-[:SCRIPT]->(script)\r\n" +
                            "MATCH (template:Component{id: {id} })-[:STYLE]->(style)\r\n" +
                            "return template, script, style",
                    params
            );
        } catch (NoSuchRecordException e) {
            throw new RuntimeException(e);
        }
    }
}
