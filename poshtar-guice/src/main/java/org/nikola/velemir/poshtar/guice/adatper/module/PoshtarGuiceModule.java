package org.nikola.velemir.poshtar.guice.adatper.module;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.nikola.velemir.poshtar.adapter.configuration.PipelineConfigurer;
import org.nikola.velemir.poshtar.guice.adatper.injection.registry.GuiceNotificationRegistry;
import org.nikola.velemir.poshtar.guice.adatper.injection.registry.GuiceRequestRegistry;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.core.mediator.PoshtarImpl;
import org.nikola.velemir.poshtar.core.notification.registry.NotificationRegistry;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.request.registry.RequestRegistry;
import org.reflections.Reflections;

import java.util.List;


public class PoshtarGuiceModule extends AbstractModule {
    private final String[] basePackages;
    private final PipelineConfigurer pipelineConfigurer;

    public PoshtarGuiceModule(PipelineConfigurer configurer, String... basePackages) {
        pipelineConfigurer = configurer;
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
    public RequestRegistry provideRequestRegistry(Injector injector){
        return new GuiceRequestRegistry(pipelineConfigurer, injector);
    }
    @Provides
    @Singleton
    public NotificationRegistry provideNotificatioNRegistry(Injector injector){
        return new GuiceNotificationRegistry(injector);
    }

    @Provides
    @Singleton
    public Poshtar configurePoshtar(RequestRegistry handlerRegistry, NotificationRegistry notificationRegistry){
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
