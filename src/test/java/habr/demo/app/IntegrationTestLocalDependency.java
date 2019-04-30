package habr.demo.app;

import org.testcontainers.containers.GenericContainer;

public interface IntegrationTestLocalDependency {

    String name();
    GenericContainer containerDefinition();
    void initializeRunningContainer(GenericContainer it);
    void initializeSystemProperties(GenericContainer it);
}
