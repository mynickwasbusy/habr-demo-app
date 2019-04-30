package habr.demo.app;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;

public class KafkaIntegrationTestLocalDependency implements IntegrationTestLocalDependency {

    @Override
    public String name() {
        return "kafka";
    }

    @Override
    public GenericContainer containerDefinition() {
        return new KafkaContainer("4.1.2");
    }

    @Override
    public void initializeRunningContainer(GenericContainer it) {

    }

    @Override
    public void initializeSystemProperties(GenericContainer it) {
        String bootstrap = ((KafkaContainer) it).getBootstrapServers();

        System.out.println("bootstrap->"+bootstrap);

        System.setProperty("kafka.topics.events", "events");
        System.setProperty("spring.kafka.bootstrapServers", bootstrap);
    }
}
