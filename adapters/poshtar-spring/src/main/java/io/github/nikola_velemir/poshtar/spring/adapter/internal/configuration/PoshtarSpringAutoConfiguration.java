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

package io.github.nikola_velemir.poshtar.spring.adapter.internal.configuration;


import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.spring.adapter.internal.mediator.SpringPoshtar;
import io.github.nikola_velemir.poshtar.spring.adapter.internal.registry.SpringRequestRegistry;
import io.github.nikola_velemir.poshtar.spring.adapter.internal.registry.SpringNotificationRegistry;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Autoconfiguration class that automatically registers the Poshtar core components into the Spring context.
 *
 * <p>
 * This class enables "plug-and-play" functionality by creating default registries
 * ({@link RequestRegistry}, {@link NotificationRegistry}) and the mediator ({@link Poshtar}).
 * Users can override any of these beans by defining their own implementations,
 * leveraging the {@link ConditionalOnMissingBean} mechanism.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@Configuration
public class PoshtarSpringAutoConfiguration {
    private final ApplicationContext context;

    /**
     * Instantiates the autoconfiguration, with the provided Spring context.
     * @param context Spring context, used for bean wiring.
     */
    public PoshtarSpringAutoConfiguration(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Provides the default {@link PipelineConfiguration} which defines the
     * execution order of behaviors within the request pipeline.
     *
     * @return A default instance of {@link PipelineConfiguration}.
     */
    @Bean
    @ConditionalOnMissingBean
    public PipelineConfiguration provideDefaultPipelineConfiguration() {
        return new PipelineConfiguration();
    }

    /**
     * Creates a {@link RequestRegistry} for the Spring environment.
     * For more details, visit {@link SpringRequestRegistry}
     *
     * @param pipelineConfiguration The pipeline order configuration, either user defined or default.
     * @return A {@link SpringRequestRegistry} implementation linked to the Spring context.
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestRegistry provideRequestRegistry(PipelineConfiguration pipelineConfiguration) {
        return new SpringRequestRegistry(context, pipelineConfiguration);
    }

    /**
     * Creates a {@link NotificationRegistry} for the Spring environment.
     * For more details, visit {@link SpringRequestRegistry}
     *
     * @return A {@link SpringNotificationRegistry} implementation linked to the Spring context.
     */
    @Bean
    @ConditionalOnMissingBean
    public NotificationRegistry provideNotificationRegistry() {
        return new SpringNotificationRegistry(context);
    }
    /**
     * The main entry point to the spring mediator. Configures the {@link Poshtar} mediator
     * to Spring context.
     *
     * @param handlerRegistry The registry containing request-to-handler mappings.
     * @param notificationRegistry The registry managing notification dispatching.
     * @return A {@link SpringPoshtar} instance.
     */
    @Bean
    @ConditionalOnMissingBean
    public Poshtar configurePoshtar(RequestRegistry handlerRegistry, NotificationRegistry notificationRegistry) {
        return new SpringPoshtar(handlerRegistry, notificationRegistry);
    }
}
