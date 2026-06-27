package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@ApplicationScoped
public class QuarkusRequestRegistry extends AbstractRequestRegistry {

    private Map<String, Class<?>> behaviourToRequestClass;

    public void init(Map<String, String> behaviourToRequest, ClassLoader cl) {
        Map<String, Class<?>> resolved = new HashMap<>();
        behaviourToRequest.forEach((behaviourName, requestName) -> {
            if (requestName == null) {
                resolved.put(behaviourName, null);
            } else {
                try {
                    resolved.put(behaviourName, Class.forName(requestName, false, cl));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Could not load behaviour request type: " + requestName, e);
                }
            }
        });
        this.behaviourToRequestClass = Map.copyOf(resolved);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerFromClass(
            Class<?> handlerClass,
            Class<?> requestClass,
            List<PipelineBehaviour<?, ?>> allBehaviours,
            BeanManager bm) {
        Bean<?> bean = bm.resolve(bm.getBeans(handlerClass));;
        if (bean == null) throw new RuntimeException("No CDI bean for handler: " + handlerClass.getName());
        RequestHandler handler = (RequestHandler) bm.getReference(
                bean, handlerClass, bm.createCreationalContext(bean));
        List<PipelineBehaviour<?, ?>> filtered = filterBehaviours(allBehaviours, requestClass);
        register((Class) requestClass, handler, filtered);
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        Class<?> clazz = behaviour.getClass();
        while (clazz != null && clazz != Object.class) {
            if (behaviourToRequestClass.containsKey(clazz.getName())) {
                Class<?> supportedClass = behaviourToRequestClass.get(clazz.getName());
                return supportedClass == null || supportedClass.isAssignableFrom(requestType);
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }
}