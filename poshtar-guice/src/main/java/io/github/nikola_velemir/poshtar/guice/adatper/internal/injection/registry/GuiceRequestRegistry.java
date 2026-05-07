package io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.registry;

import com.google.common.reflect.TypeToken;
import com.google.inject.Binding;
import com.google.inject.Injector;
import io.github.nikola_velemir.poshtar.adapter.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import jdk.jshell.spi.ExecutionControl;
import org.checkerframework.checker.nullness.qual.NonNull;


import java.util.ArrayList;
import java.util.List;

/**
 * Class maps specific request type to its chain of behaviors supported by generic constraint, ending with a handler.
 * Note that behavior and handler classes can be proxies.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class GuiceRequestRegistry extends AbstractRequestRegistry {
    private final PipelineConfiguration pipelineConfiguration;

    public GuiceRequestRegistry(PipelineConfiguration configurer, Injector injector) {
        this.pipelineConfiguration = configurer;
        init(injector);

    }

    private void init(Injector injector) {

        List<? extends PipelineBehaviour<?, ?>> orderedBehaviours = provideBehaviours(injector);
        List<RequestHandler> allHandlers = provideHandlers(injector);
        for (RequestHandler<?, ?> handler : allHandlers) {
            TypeToken<?> typeToken = TypeToken.of(handler.getClass());

            TypeToken<?> superType = typeToken.getSupertype((Class) RequestHandler.class);

            Class<?> requestType = superType.resolveType(RequestHandler.class.getTypeParameters()[0]).getRawType();
            if(!Request.class.isAssignableFrom(requestType)) continue;

            List<PipelineBehaviour<?,?>> filteredBehaviours = filterBehaviours((List<PipelineBehaviour<?, ?>>) orderedBehaviours,requestType);

            Class<Request<Object>> castedRequest = (Class<Request<Object>>) requestType;
            RequestHandler<Request<Object>, Object> castedHandler = (RequestHandler<Request<Object>, Object>) handler;

            register(castedRequest, castedHandler, filteredBehaviours);
        }
    }

    private @NonNull List<? extends PipelineBehaviour<?, ?>> provideBehaviours(Injector injector) {
        return pipelineConfiguration
                .getBehaviourClasses()
                .stream()
                .map(injector::getInstance)
                .toList();
    }

    private static List<RequestHandler> provideHandlers(Injector injector) {
        List<RequestHandler> allHandlers = new ArrayList<>();
        for (Binding<?> binding : injector.getAllBindings().values()) {
            Class<?> rawType = binding.getKey().getTypeLiteral().getRawType();
            if (RequestHandler.class.isAssignableFrom(rawType) && !rawType.isInterface()) {
                allHandlers.add((RequestHandler) injector.getInstance(binding.getKey()));
            }
        }
        return allHandlers;
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) throws ExecutionControl.NotImplementedException {
        TypeToken<?> typeToken = TypeToken.of(behaviour.getClass());
        TypeToken<?> superType = typeToken.getSupertype((Class) PipelineBehaviour.class);
        Class<?> genericRequestType = superType.resolveType(PipelineBehaviour.class.getTypeParameters()[0]).getRawType();

        return genericRequestType.isAssignableFrom(requestType);
    }
}
