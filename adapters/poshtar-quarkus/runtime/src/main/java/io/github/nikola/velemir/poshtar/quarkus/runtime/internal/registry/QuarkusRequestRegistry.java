package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class QuarkusRequestRegistry extends AbstractRequestRegistry {
    private Map<String, String> behaviourToRequest;
    public void init(Map<String, String> behaviourToRequest) {
        this.behaviourToRequest = behaviourToRequest;
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerFromClass(
            Class<?> handlerClass,
            Class<?> requestClass,
            List<PipelineBehaviour<?, ?>> allBehaviours,
            BeanManager bm) {

        Bean<?> bean = bm.getBeans(handlerClass).stream().findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No CDI bean for handler: " + handlerClass.getName()));
        RequestHandler handler = (RequestHandler) bm.getReference(
                bean, handlerClass, bm.createCreationalContext(bean));

        List<PipelineBehaviour<?, ?>> filtered = filterBehaviours(allBehaviours, requestClass);
        register((Class) requestClass, handler, filtered);
        System.out.println("[PoshtaR] Registered handler: " + handlerClass.getSimpleName()
                + " -> " + requestClass.getSimpleName());
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        // Walk up past any Arc proxy subclass to find the real class name
        Class<?> clazz = behaviour.getClass();
        while (clazz != null && clazz != Object.class) {
            if (behaviourToRequest.containsKey(clazz.getName())) {
                String supportedType = behaviourToRequest.get(clazz.getName());
                if (supportedType == null) return true; // global behaviour
                try {
                    Class<?> supportedClass = Class.forName(supportedType,
                            false, Thread.currentThread().getContextClassLoader());
                    return supportedClass.isAssignableFrom(requestType);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Could not load behaviour request type: " + supportedType, e);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }
}