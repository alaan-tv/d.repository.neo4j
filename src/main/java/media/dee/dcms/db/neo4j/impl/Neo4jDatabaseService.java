package media.dee.dcms.db.neo4j.impl;

import media.dee.dcms.core.db.GraphDatabaseService;
import media.dee.dcms.core.db.NoSuchRecordException;
import org.neo4j.driver.v1.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component(
        immediate = true,
        name = "media.dee.dcms.db.neo4j.service",
        property = {
                "username=neo4j",
                "password=neo4j",
                "url=bolt://localhost",
                "sessionMax:Integer=100",
                "livenessCheckout:Integer=30000"
        }
)
public class Neo4jDatabaseService implements GraphDatabaseService<GraphRecord> {

    private Driver driver;

    @Activate
    void activate(Map<String, Object> properties) {
        String username = (String) properties.get("username");
        String password = (String) properties.get("password");
        String url = (String) properties.get("url");
        int sessionMax = Integer.parseInt(properties.get("sessionMax").toString());
        int livenessCheckout = Integer.parseInt(properties.get("livenessCheckout").toString());


        Config config = Config.build()
                .withMaxIdleSessions(sessionMax)
                .withEncryption()
                .withConnectionLivenessCheckTimeout(livenessCheckout, TimeUnit.MINUTES)
                .toConfig();

        if( driver != null ) {
            driver.close();
            driver = null;
        }

        driver = GraphDatabase.driver(url,
                AuthTokens.basic(username, password),
                config);
    }

    @Deactivate
    void deactivate() {
        if(driver != null )
            driver.close();
        driver = null;
    }


    @Override
    public void run(String query, Map<String, Object> parameters, Consumer<GraphRecord> consumer) {

        try(Session session = driver.session(); Transaction transaction = session.beginTransaction() ){
            StatementResult result = transaction.run( query , parameters);
            while ( result.hasNext() )
                consumer.accept(new GraphRecord(result.next()));
            transaction.success();
        }

    }

    @Override
    public List<GraphRecord> run(String query, Map<String, Object> parameters) {
        List<GraphRecord> result = null;
        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            StatementResult queryResult = transaction.run( query , parameters);
            result = queryResult.list().stream()
                .map( GraphRecord::new )
                .collect(Collectors.toList());
            transaction.success();
        }
        return result;
    }

    @Override
    public void fetchOne(String query, Map<String, Object> parameters, Consumer<GraphRecord> consumer) throws NoSuchRecordException {

        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            StatementResult result = transaction.run( query , parameters);
            consumer.accept(new GraphRecord(result.next()));
            transaction.success();
        }

    }

    @Override
    public <T> T fetchOne(String query, Map<String, Object> parameters, Function<GraphRecord, T> mapper) throws NoSuchRecordException {
        T result;
        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            result = mapper.apply(new GraphRecord(transaction.run( query , parameters).single()));
            transaction.success();
        }
        return result;
    }

    @Override
    public GraphRecord fetchOne(String query, Map<String, Object> parameters) throws NoSuchRecordException {
        try( Session session = driver.session() ; Transaction transaction = session.beginTransaction() ){
            StatementResult result = transaction.run( query , parameters);
            org.neo4j.driver.v1.Record record = result.single();
            transaction.success();
            return new GraphRecord(record);
        }
    }
}
