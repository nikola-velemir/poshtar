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
            Map<String, String> notificationHandlerToNotification,
            Map<String, List<String>> handlerToBehaviours) {

        BeanManager bm = Arc.container().beanManager();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        QuarkusRequestRegistry requestRegistry = Arc.container()
                .instance(QuarkusRequestRegistry.class).get();
        QuarkusNotificationRegistry notificationRegistry = Arc.container()
                .instance(QuarkusNotificationRegistry.class).get();

        PipelineConfiguration pipelineConfiguration = Arc.container()
                .instance(PipelineConfiguration.class).get();

        List<Class<? extends PipelineBehaviour<?, ?>>> orderedBehaviourClasses = pipelineConfiguration.getBehaviourClasses();
        Map<String, PipelineBehaviour<?, ?>> behaviourProxies = extractBehaviourProxies(orderedBehaviourClasses, bm);


        registerRequestMappings(handlerToRequest, handlerToBehaviours, orderedBehaviourClasses, behaviourProxies, requestRegistry, bm, cl);
        registerNotificationMappings(notificationHandlerToNotification, notificationRegistry, bm, cl);
    }

    @Nonnull
    private static Map<String, PipelineBehaviour<?, ?>> extractBehaviourProxies(List<Class<? extends PipelineBehaviour<?, ?>>> orderedBehaviourClasses, BeanManager bm) {
        Map<String, PipelineBehaviour<?, ?>> behaviourProxies = new java.util.LinkedHashMap<>();
        for (Class<?> clazz : orderedBehaviourClasses) {
            Bean<?> bean = bm.resolve(bm.getBeans(clazz));
            if (bean == null) throw new RuntimeException("No CDI bean for behaviour: " + clazz.getName());
            behaviourProxies.put(clazz.getName(), (PipelineBehaviour<?, ?>) bm.getReference(
                    bean, clazz, bm.createCreationalContext(bean)));
        }
        return behaviourProxies;
    }

    @SuppressWarnings("unchecked")
    private void registerRequestMappings(
            Map<String, String> handlerToRequest,
            Map<String, List<String>> handlerToBehaviours,
            List<Class<? extends PipelineBehaviour<?, ?>>> orderedBehaviourClasses,
            Map<String, PipelineBehaviour<?, ?>> behaviourProxies,
            QuarkusRequestRegistry requestRegistry,
            BeanManager bm,
            ClassLoader cl) {

        handlerToRequest.forEach((handlerName, requestName) -> {
            Class<?> handlerClass = loadClass(handlerName, cl);
            Class<?> requestClass = loadClass(requestName, cl);


            List<String> applicableBehaviourNames = handlerToBehaviours.getOrDefault(handlerName, List.of());
            List<? extends PipelineBehaviour<?, ?>> filteredBehaviours = orderedBehaviourClasses.stream()
                    .filter(bc -> applicableBehaviourNames.contains(bc.getName()))
                    .map(bc -> behaviourProxies.get(bc.getName()))
                    .toList();

            requestRegistry.registerFromClass(handlerClass, requestClass, (List<PipelineBehaviour<?, ?>>) filteredBehaviours, bm);
        });
    }

    private void registerNotificationMappings(
            Map<String, String> notificationHandlerToNotification,
            QuarkusNotificationRegistry notificationRegistry,
            BeanManager bm,
            ClassLoader cl) {

        notificationHandlerToNotification.forEach((handlerName, notifName) -> {
            Class<?> handlerClass = loadClass(handlerName, cl);
            Class<?> notifClass = loadClass(notifName, cl);
            notificationRegistry.registerFromClass(handlerClass, notifClass, bm);
        });
    }

    private Class<?> loadClass(String name, ClassLoader cl) {
        try {
            return Class.forName(name, true, cl);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class: " + name, e);
        }
    }

}