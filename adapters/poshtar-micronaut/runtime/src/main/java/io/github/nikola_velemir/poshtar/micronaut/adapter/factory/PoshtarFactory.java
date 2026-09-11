/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.micronaut.adapter.factory;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.mediator.Publisher;
import io.github.nikola_velemir.poshtar.core.mediator.Sender;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.micronaut.adapter.internal.mediator.MicronautPoshtar;
import io.github.nikola_velemir.poshtar.micronaut.adapter.internal.registry.MicronautNotificationRegistry;
import io.github.nikola_velemir.poshtar.micronaut.adapter.internal.registry.MicronautRequestRegistry;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

import java.util.List;

/**
 * Produces the core Poshtar beans for a Micronaut application: the default pipeline
 * configuration (when the consumer hasn't supplied their own), both registries, and
 * the top-level {@link Poshtar} facade that consumers actually inject and use.
 *
 * @author Nikola Velemir
 * @version ${revision}
 * @since 1.0.0
 */
@Factory
public class PoshtarFactory {

    /**
     * Supplies an empty {@link PipelineConfiguration} — no behaviours, handler-only pipelines —
     * so the library works out of the box with zero required setup. Only activates when the
     * consumer hasn't declared their own {@link PipelineConfiguration} bean; theirs always wins.
     *
     * @return an empty pipeline configuration.
     */
    @Singleton
    @Requires(missingBeans = PipelineConfiguration.class)
    public PipelineConfiguration defaultPipelineConfiguration() {
        return new PipelineConfiguration();

    }

    /**
     * Produces the request registry, wiring discovered
     * {@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler} beans
     * against the supplied pipeline configuration.
     *
     * @param context               the Micronaut bean context, used for handler/behaviour discovery.
     * @param pipelineConfiguration the ordered behaviour configuration, defaulted above if absent.
     * @return the constructed request registry.
     */
    @Singleton
    public RequestRegistry requestRegistry(BeanContext context, PipelineConfiguration pipelineConfiguration) {
        return new MicronautRequestRegistry(context, pipelineConfiguration);
    }

    /**
     * Produces the notification registry, wiring discovered
     * {@link NotificationHandler} beans.
     *
     * @param context the Micronaut bean context, used for handler discovery.
     * @return the constructed notification registry.
     */
    @Singleton
    public NotificationRegistry notificationRegistry(BeanContext context) {
        return new MicronautNotificationRegistry(context);
    }



    /**
     * Produces the top-level {@link Poshtar} facade — the single entry point consumers
     * inject to send requests and publish notifications.
     *
     * @param requestRegistry      resolves requests to their handler pipelines.
     * @param notificationRegistry resolves notifications to their handlers.
     * @return the constructed Poshtar facade.
     */
    @Singleton
    public Poshtar poshtar(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry) {
        return new MicronautPoshtar(requestRegistry, notificationRegistry);
    }
    @Singleton
    public Sender sender(Poshtar poshtar) {
        return poshtar;
    }

    @Singleton
    public Publisher publisher(Poshtar poshtar) {
        return poshtar;
    }
}