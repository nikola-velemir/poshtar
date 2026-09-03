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