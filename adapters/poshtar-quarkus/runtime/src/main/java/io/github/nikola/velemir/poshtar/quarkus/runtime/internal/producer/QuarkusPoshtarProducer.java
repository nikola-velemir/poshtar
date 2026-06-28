package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.producer;

import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.mediator.PoshtarImpl;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Singleton
public class QuarkusPoshtarProducer {

    /**
     * Default PipelineConfiguration — user can override by declaring
     * their own @Produces PipelineConfiguration bean.
     */
    @Produces
    @DefaultBean
    @ApplicationScoped
    public PipelineConfiguration pipelineConfiguration() {
        return new PipelineConfiguration();
    }

    /**
     * Wires the Poshtar mediator from the two registries.
     * Equivalent to PoshtarSpringAutoConfiguration#configurePoshtar.
     */
    @Produces
    @DefaultBean
    @ApplicationScoped
    public Poshtar poshtar(RequestRegistry requestRegistry,
                           NotificationRegistry notificationRegistry) {
        return new PoshtarImpl(requestRegistry, notificationRegistry);
    }
}