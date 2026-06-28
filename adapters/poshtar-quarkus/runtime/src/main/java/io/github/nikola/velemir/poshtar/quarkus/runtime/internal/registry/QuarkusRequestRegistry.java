package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception.BeanNotFoundException;
import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception.SupportsOverrideForbidden;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import java.util.List;

@ApplicationScoped
public class QuarkusRequestRegistry extends AbstractRequestRegistry {


    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerFromClass(
            Class<?> handlerClass,
            Class<?> requestClass,
            List<PipelineBehaviour<?, ?>> filteredBehaviours,
            BeanManager bm) {

        Bean<?> bean = bm.resolve(bm.getBeans(handlerClass));
        if (bean == null) throw new BeanNotFoundException(handlerClass);

        RequestHandler handler = (RequestHandler) bm.getReference(
                bean, handlerClass, bm.createCreationalContext(bean));

        register((Class) requestClass, handler, filteredBehaviours);
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        throw new SupportsOverrideForbidden();
    }
}