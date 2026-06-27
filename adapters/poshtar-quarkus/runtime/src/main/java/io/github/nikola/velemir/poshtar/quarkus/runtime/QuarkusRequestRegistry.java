package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InjectableBean;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@ApplicationScoped
public class QuarkusRequestRegistry extends AbstractRequestRegistry {

    @Inject
    @Any
    @SuppressWarnings("rawtypes")
    Instance<RequestHandler> handlers;

    @Inject
    Instance<PipelineBehaviour<?, ?>> behaviours;

    @Inject
    PipelineConfiguration pipelineConfiguration;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void initFromClassNames(List<String> classNames) {
        List<? extends PipelineBehaviour<?, ?>> orderedBehaviours = provideBehaviours();

        for (String className : classNames) {
            try {
                Class<?> beanClass = Class.forName(className);
                RequestHandler handler = (RequestHandler) Arc.container()
                        .instance(beanClass)
                        .get();

                if (handler == null) continue;

                for (Type iface : beanClass.getGenericInterfaces()) {
                    if (!(iface instanceof ParameterizedType pt)) continue;
                    if (!pt.getRawType().equals(RequestHandler.class)) continue;

                    Type arg = pt.getActualTypeArguments()[0];
                    if (arg instanceof Class<?> requestType
                            && Request.class.isAssignableFrom(requestType)) {
                        List<PipelineBehaviour<?, ?>> filtered =
                                filterBehaviours((List<PipelineBehaviour<?, ?>>) orderedBehaviours, requestType);
                        registerHandler((Class) requestType, handler, filtered);
                    }
                    break;
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not load handler class: " + className, e);
            }
        }
    }

    private <TRequest extends Request<TResponse>, TResponse> void registerHandler(
            Class<TRequest> requestType,
            RequestHandler<TRequest, TResponse> handler,
            List<PipelineBehaviour<?, ?>> behaviours) {
        register(requestType, handler, behaviours);
    }

    private List<? extends PipelineBehaviour<?, ?>> provideBehaviours() {
        return pipelineConfiguration
                .getBehaviourClasses()
                .stream()
                .map(clazz -> behaviours.select(clazz).get())
                .toList();
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        InjectableBean<?> injectableBean = Arc.container()
                .bean(behaviour.getClass().getName());

        if (injectableBean == null) return false;

        Class<?> beanClass = injectableBean.getBeanClass();

        for (Type iface : beanClass.getGenericInterfaces()) {
            if (!(iface instanceof ParameterizedType pt)) continue;
            if (!pt.getRawType().equals(PipelineBehaviour.class)) continue;

            Type arg = pt.getActualTypeArguments()[0];
            if (arg instanceof Class<?> genericRequestType) {
                return genericRequestType.isAssignableFrom(requestType);
            }
            return true; // wildcard or type variable — supports everything
        }
        return false;
    }
}