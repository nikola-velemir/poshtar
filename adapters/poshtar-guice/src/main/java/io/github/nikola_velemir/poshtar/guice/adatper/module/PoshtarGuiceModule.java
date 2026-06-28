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

package io.github.nikola_velemir.poshtar.guice.adatper.module;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.mediator.GuicePoshtar;
import io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.registry.GuiceNotificationRegistry;
import io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.registry.GuiceRequestRegistry;

import org.reflections.Reflections;

import java.util.List;

/**
 * Guice module that configures and initializes the Poshtar mediator.
 * <p>
 * This module scans the specified base packages for classes annotated with {@link Handler}
 * and automatically binds them as Singletons. It also sets up the internal registries
 * and provides the {@link Poshtar} component for injection.
 * </p>
 *
 * <p>The module performs the following operations:</p>
 * <ul>
 *     <li>Scans the classpath using the {@code Reflections} library.</li>
 *     <li>Binds all discovered {@link Handler} classes to the Guice container.</li>
 *     <li>Binds all globally configured {@link PipelineBehaviour} classes.</li>
 *     <li>Registers {@link RequestRegistry} and {@link NotificationRegistry} using Guice-specific implementations.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * public class MyApplicationModule extends AbstractModule {
 *     &#64;Override
 *     protected void configure() {
 *         // 1. Set up the pipeline configuration (optional)
 *         PipelineConfiguration configuration = new PipelineConfiguration()
 *             .add(LoggingBehaviour.class)
 *             .add(ValidationBehaviour.class);
 *
 *         // 2. Install the Poshtar module with the configuration and scan packages
 *         install(new PoshtarGuiceModule(configuration, "com.mycompany.app.features"));
 *     }
 * }
 * </pre>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class PoshtarGuiceModule extends AbstractModule {
    private final String[] basePackages;
    private final PipelineConfiguration pipelineConfiguration;
    /**
     * Constructs a module with custom pipeline configuration and scan targets.
     *
     * @param configuration The pipeline configuration defining behavior order.
     * @param basePackages  The packages to scan for handlers and behaviors; if empty, scans the entire classpath.
     */
    public PoshtarGuiceModule(PipelineConfiguration configuration, String... basePackages) {
        pipelineConfiguration = configuration;
        this.basePackages = basePackages.length > 0 ? basePackages : new String[]{""};
    }
    /**
     * Constructs a module with default configuration and specific scan targets.
     *
     * @param basePackages The packages to scan for handlers; if empty, scans the entire classpath.
     */
    public PoshtarGuiceModule(String... basePackages) {
        pipelineConfiguration = new PipelineConfiguration();
        this.basePackages = basePackages.length > 0 ? basePackages : new String[]{""};
    }
    /**
     * Configures the Guice bindings by performing classpath scanning for handlers
     * and registering configured behaviors.
     */
    @Override
    protected void configure() {
        Reflections reflections = new Reflections((Object[]) basePackages);
        bindHandlers(reflections);
        bindBehaviours();
    }
    /**
     * Provides a singleton instance of the RequestRegistry.
     *
     * @param injector The Guice injector used to resolve handlers.
     * @return A configured GuiceRequestRegistry.
     */
    @Provides
    @Singleton
    public RequestRegistry provideRequestRegistry(Injector injector) {
        return new GuiceRequestRegistry(pipelineConfiguration, injector);
    }
    /**
     * Provides a singleton instance of the NotificationRegistry.
     *
     * @param injector The Guice injector used to resolve handlers.
     * @return A configured GuiceNotificationRegistry.
     */
    @Provides
    @Singleton
    public NotificationRegistry provideNotificationRegistry(Injector injector) {
        return new GuiceNotificationRegistry(injector);
    }
    /**
     * Provides the Poshtar mediator instance.
     *
     * @param handlerRegistry      The registry for request handling.
     * @param notificationRegistry The registry for notification broadcasting.
     * @return The singleton Poshtar implementation.
     */
    @Provides
    @Singleton
    public Poshtar providePoshtar(RequestRegistry handlerRegistry, NotificationRegistry notificationRegistry) {
        return new GuicePoshtar(handlerRegistry, notificationRegistry);
    }

    private void bindBehaviours() {
        if (pipelineConfiguration == null) return;

        List<Class<? extends PipelineBehaviour<?, ?>>> behaviourClasses = pipelineConfiguration.getBehaviourClasses();
        for (Class<? extends PipelineBehaviour<?, ?>> behaviourClass : behaviourClasses) {
            bind(behaviourClass).in(Singleton.class);
        }

    }

    private void bindHandlers(Reflections reflections) {
        var handlerClasses = reflections.getTypesAnnotatedWith(Handler.class);
        for (Class<?> handlerClass : handlerClasses) {
            bind(handlerClass).in(Singleton.class);
        }
    }
}
