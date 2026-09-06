package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.producer;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.mediator.QuarkusPoshtarImpl;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

/**
 * Producer class, containing base bindings for PoshtaR components
 */
@Singleton
public class QuarkusPoshtarProducer {

    /**
     * Default {@link PipelineConfiguration} — user can override by declaring
     * their own @Produces {@link PipelineConfiguration} bean.
     */
    @Produces
    @DefaultBean
    @ApplicationScoped
    public PipelineConfiguration pipelineConfiguration() {
        return new PipelineConfiguration();
    }

    /**
     * Wires the Poshtar mediator from the two registries, to be provided as CDI bean.
     *
     * @param requestRegistry      Request registry bean
     * @param notificationRegistry Notification registry
     * @return Concrete {@link Poshtar} implementation (instance of {@link QuarkusPoshtarImpl})
     */
    @Produces
    @DefaultBean
    @ApplicationScoped
    public Poshtar poshtar(RequestRegistry requestRegistry,
                           NotificationRegistry notificationRegistry) {
        return new QuarkusPoshtarImpl(requestRegistry, notificationRegistry);
    }
}