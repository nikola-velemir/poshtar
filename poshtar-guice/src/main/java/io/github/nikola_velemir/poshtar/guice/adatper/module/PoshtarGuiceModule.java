package io.github.nikola_velemir.poshtar.guice.adatper.module;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.nikola_velemir.poshtar.adapter.configuration.PipelineConfigurer;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.mediator.PoshtarImpl;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.registry.GuiceNotificationRegistry;
import io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.registry.GuiceRequestRegistry;

import org.reflections.Reflections;

import java.util.List;


public class PoshtarGuiceModule extends AbstractModule {
    private final String[] basePackages;
    private final PipelineConfigurer pipelineConfigurer;

    public PoshtarGuiceModule(PipelineConfigurer configurer, String... basePackages) {
        pipelineConfigurer = configurer;
        this.basePackages = basePackages.length > 0 ? basePackages : new String[]{""};
    }

    public PoshtarGuiceModule(String... basePackages) {
        pipelineConfigurer = new PipelineConfigurer();
        this.basePackages = basePackages.length > 0 ? basePackages : new String[]{""};
    }

    @Override
    protected void configure() {
        Reflections reflections = new Reflections((Object[]) basePackages);
        bindHandlers(reflections);
        bindBehaviours();
    }

    @Provides
    @Singleton
    public RequestRegistry provideRequestRegistry(Injector injector) {
        return new GuiceRequestRegistry(pipelineConfigurer, injector);
    }

    @Provides
    @Singleton
    public NotificationRegistry provideNotificationRegistry(Injector injector) {
        return new GuiceNotificationRegistry(injector);
    }

    @Provides
    @Singleton
    public Poshtar providePoshtar(RequestRegistry handlerRegistry, NotificationRegistry notificationRegistry) {
        return new PoshtarImpl(handlerRegistry, notificationRegistry);
    }

    private void bindBehaviours() {
        if (pipelineConfigurer == null) return;

        List<Class<? extends PipelineBehaviour<?, ?>>> behaviourClasses = pipelineConfigurer.getBehaviourClasses();
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
