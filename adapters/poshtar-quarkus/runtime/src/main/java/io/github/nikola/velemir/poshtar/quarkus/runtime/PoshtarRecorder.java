package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import java.util.List;

@Recorder
public class PoshtarRecorder {
    @SuppressWarnings("unchecked")
    public void initRegistries(
            List<String> handlerClassNames,
            List<String> notificationHandlerClassNames) {
        BeanManager bm = Arc.container().beanManager();

        QuarkusRequestRegistry requestRegistry = Arc.container()
                .instance(QuarkusRequestRegistry.class).get();
        QuarkusNotificationRegistry notificationRegistry = Arc.container()
                .instance(QuarkusNotificationRegistry.class).get();

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        PipelineConfiguration pipelineConfiguration = Arc.container()
                .instance(PipelineConfiguration.class).get();
        List<? extends PipelineBehaviour<?, ?>> behaviours = pipelineConfiguration.getBehaviourClasses()
                .stream()
                .map(clazz -> {
                    Bean<?> bean = bm.getBeans(clazz).stream().findFirst()
                            .orElseThrow(() -> new RuntimeException(
                                    "No CDI bean for behaviour: " + clazz.getName()));
                    return (PipelineBehaviour<?, ?>) bm.getReference(
                            bean, clazz, bm.createCreationalContext(bean));
                })
                .toList();

        handlerClassNames.stream()
                .map(name -> loadClass(name, cl))
                .forEach(clazz -> requestRegistry.registerFromClass(clazz, (List<PipelineBehaviour<?, ?>>) behaviours, bm));

        notificationHandlerClassNames.stream()
                .map(name -> loadClass(name, cl))
                .forEach(clazz -> notificationRegistry.registerFromClass(clazz, bm));
    }

    private Class<?> loadClass(String name, ClassLoader cl) {
        try {
            return Class.forName(name, true, cl);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class: " + name, e);
        }
    }
}