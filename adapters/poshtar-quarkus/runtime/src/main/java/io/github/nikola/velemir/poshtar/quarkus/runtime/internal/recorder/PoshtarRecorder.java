package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.recorder;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry.QuarkusNotificationRegistry;
import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry.QuarkusRequestRegistry;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import java.util.List;
import java.util.Map;

@Recorder
public class PoshtarRecorder {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void initRegistries(
            Map<String, String> handlerToRequest,
            Map<String, String> notificationHandlerToNotification, Map<String, String> behaviourToRequest) {

        BeanManager bm = Arc.container().beanManager();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        QuarkusRequestRegistry requestRegistry = Arc.container()
                .instance(QuarkusRequestRegistry.class).get();
        QuarkusNotificationRegistry notificationRegistry = Arc.container()
                .instance(QuarkusNotificationRegistry.class).get();

        PipelineConfiguration pipelineConfiguration = Arc.container()
                .instance(PipelineConfiguration.class).get();

        List<? extends PipelineBehaviour<?, ?>> behaviours = extractBehaviours(pipelineConfiguration, bm);

        requestRegistry.init(behaviourToRequest);

        registerRequestMappings(handlerToRequest, cl, requestRegistry, (List<PipelineBehaviour<?, ?>>) behaviours, bm);

        registerNotificationMappings(notificationHandlerToNotification, cl, notificationRegistry, bm);
    }

    private void registerNotificationMappings(Map<String, String> notificationHandlerToNotification, ClassLoader cl, QuarkusNotificationRegistry notificationRegistry, BeanManager bm) {
        notificationHandlerToNotification.forEach((handlerName, notifName) -> {
            Class<?> handlerClass = loadClass(handlerName, cl);
            Class<?> notifClass = loadClass(notifName, cl);
            notificationRegistry.registerFromClass(handlerClass, notifClass, bm);
        });
    }

    private void registerRequestMappings(Map<String, String> handlerToRequest, ClassLoader cl, QuarkusRequestRegistry requestRegistry, List<PipelineBehaviour<?, ?>> behaviours, BeanManager bm) {
        handlerToRequest.forEach((handlerName, requestName) -> {
            Class<?> handlerClass = loadClass(handlerName, cl);
            Class<?> requestClass = loadClass(requestName, cl);
            requestRegistry.registerFromClass(handlerClass, requestClass, behaviours, bm);
        });
    }

    @Nonnull
    private static List<? extends PipelineBehaviour<?, ?>> extractBehaviours(PipelineConfiguration pipelineConfiguration, BeanManager bm) {
        return pipelineConfiguration.getBehaviourClasses()
                .stream()
                .map(clazz -> {
                    Bean<?> bean = bm.getBeans(clazz).stream().findFirst()
                            .orElseThrow(() -> new RuntimeException(
                                    "No CDI bean for behaviour: " + clazz.getName()));
                    return (PipelineBehaviour<?, ?>) bm.getReference(
                            bean, clazz, bm.createCreationalContext(bean));
                })
                .toList();
    }

    private Class<?> loadClass(String name, ClassLoader cl) {
        try {
            return Class.forName(name, true, cl);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class: " + name, e);
        }
    }
}