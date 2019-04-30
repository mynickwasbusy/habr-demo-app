package habr.demo.app;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;

public class CassandraIntegrationTestLocalDependency implements IntegrationTestLocalDependency {

    private static final String KEYSPACE = "test";

    @Override
    public String name() {
        return "cassandra";
    }

    @Override
    public GenericContainer containerDefinition() {
        return new CassandraContainer();
    }

    @Override
    public void initializeRunningContainer(GenericContainer it) {

        try (
                Cluster cluster = createClusterConnectionFor(it);
                Session session = cluster.connect();
        ) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS test" + " WITH replication = {" +
                    "'class':'" + "SimpleStrategy" +
                    "','replication_factor':" + 1 +
                    "};");

            session.execute("CREATE TABLE IF NOT EXISTS test.events" + "(" +
                    "user_id uuid, " +
                    "id uuid, " +
                    "time bigint, " +
                    "type varchar," +
                    "PRIMARY KEY ((user_id), id)" +
                    ");");
        }
    }

    @Override
    public void initializeSystemProperties(GenericContainer it) {
        System.setProperty("cassandra.host", it.getContainerIpAddress());
        System.setProperty("cassandra.port", Integer.toString(it.getMappedPort(9042)));
        System.setProperty("cassandra.keyspace", KEYSPACE);
    }

    private Cluster createClusterConnectionFor(GenericContainer it) {
        return Cluster.builder()
                .withoutMetrics()
                .withoutJMXReporting()
                .addContactPoint(it.getContainerIpAddress())
                .withPort(it.getMappedPort(9042))
                .build();
    }
}
