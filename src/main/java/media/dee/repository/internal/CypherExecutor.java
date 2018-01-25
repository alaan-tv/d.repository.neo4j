package media.dee.repository.internal;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.neo4j.driver.v1.types.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class CypherExecutor {

    private Driver driver;

    public CypherExecutor(String url, String username, String password, int maxSessions, int sessionTimeout ) {

        Config config = Config.build()
                              .withMaxIdleSessions(maxSessions)
                              .withEncryption()
                              .withConnectionLivenessCheckTimeout(sessionTimeout, TimeUnit.MINUTES)
                              .toConfig();


        driver = GraphDatabase.driver(url,
                AuthTokens.basic(username, password),
                config);
    }

    public void run(String query, Map<String, Object> parameters, Consumer<Record> consumer) {
        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            StatementResult result = transaction.run( query , parameters);
            while ( result.hasNext() )
                consumer.accept(result.next());
            transaction.success();
        }
    }

    public List<Record> run(String query, Map<String, Object> parameters) {
        List<Record> result = null;
        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            StatementResult queryResult = transaction.run( query , parameters);
            result = queryResult.list();
            transaction.success();
        }
        return result;
    }


    public void fetchOne(String query, Map<String, Object> parameters, Consumer<Record> consumer) throws NoSuchRecordException {
        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            StatementResult result = transaction.run( query , parameters);
            consumer.accept(result.single());
            transaction.success();
        }
    }


    public <T> T fetchOne(String query, Map<String, Object> parameters, Function<Record, T> consumer) throws NoSuchRecordException {
        T result;
        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            result = consumer.apply(transaction.run( query , parameters).single());
            transaction.success();
        }
        return result;
    }


    public Record fetchOne(String query, Map<String, Object> parameters) throws NoSuchRecordException {
        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            StatementResult result = transaction.run( query , parameters);
            Record record = result.single();
            transaction.success();
            return record;
        }
    }


    public String getNodeLabel(Node node, String... execlude) throws NoSuchRecordException {
        List<String> excludeLst = Arrays.asList(execlude);
        for( String label : node.labels() )
            if( ! excludeLst.contains(label) )
                return label;
        return null;
    }

    public Map<String, Object> mapNode(Node node) {
        HashMap<String, Object> result = new HashMap<>(node.asMap(n -> n.asObject() ));
        result.put("id", node.id());
        return result;
    }

    public void close(){
        driver.close();
    }

}
