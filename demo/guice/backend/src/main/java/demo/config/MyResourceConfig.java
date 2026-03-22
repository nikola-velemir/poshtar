package demo.config;

import jakarta.inject.Inject; // Make sure it's jakarta.inject, not com.google.injectimport org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

public class MyResourceConfig extends ResourceConfig {
    @Inject
    public MyResourceConfig(ServiceLocator serviceLocator) {
        packages("demo");

        register(CorsFilter.class);
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        if (ServletConfig.injector != null) {
            guiceBridge.bridgeGuiceInjector(ServletConfig.injector);
        }
    }
}