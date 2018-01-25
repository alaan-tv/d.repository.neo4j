package media.dee.repository.activator;
import media.dee.repository.internal.CypherExecutor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class RepositoryBundleActivator implements BundleActivator {
    private ServiceRegistration registration;

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        String url = bundleContext.getProperty("Neo4j.URL");
        String username = bundleContext.getProperty("Neo4j.username");
        String password = bundleContext.getProperty("Neo4j.password");
        int maxSessions = Integer.parseInt(bundleContext.getProperty("Neo4j.Session.Max"));
        int sessionTimeout = Integer.parseInt(bundleContext.getProperty("Neo4j.Session.LivenessCheckout"));
        registration = bundleContext.registerService(CypherExecutor.class.getName(), new CypherExecutor(url, username, password,maxSessions, sessionTimeout),null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        CypherExecutor cypherExecutor = (CypherExecutor) registration.getReference();
        cypherExecutor.close();
        registration.unregister();
    }
}
