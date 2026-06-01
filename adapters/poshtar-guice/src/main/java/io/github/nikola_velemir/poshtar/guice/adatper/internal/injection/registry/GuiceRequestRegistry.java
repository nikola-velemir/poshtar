/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.registry;

import com.google.common.reflect.TypeToken;
import com.google.inject.Injector;
import com.google.inject.Key;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
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
@SuppressWarnings({"rawtypes", "unchecked"})
public class GuiceRequestRegistry extends AbstractRequestRegistry {
    private final PipelineConfiguration pipelineConfiguration;

    /**
     * Instantiates the registry, with the provided Guice injector.
     *
     * @param injector      Guice injector, used to discover classes thru bindings.
     * @param configuration Provided order of behavior execution.
     */
    public GuiceRequestRegistry(PipelineConfiguration configuration, Injector injector) {
        this.pipelineConfiguration = configuration;
        init(injector);

    }

    private void init(Injector injector) {

        List<? extends PipelineBehaviour<?, ?>> orderedBehaviours = provideBehaviours(injector);
        List<RequestHandler> allHandlers = provideHandlers(injector);
        for (RequestHandler<?, ?> handler : allHandlers) {
            TypeToken<?> typeToken = TypeToken.of(GuiceProxyUtils.resolveTargetClass(handler));

            TypeToken<?> superType = typeToken.getSupertype((Class) RequestHandler.class);

            Class<?> requestType = superType.resolveType(RequestHandler.class.getTypeParameters()[0]).getRawType();
            if (!Request.class.isAssignableFrom(requestType)) continue;

            List<PipelineBehaviour<?, ?>> filteredBehaviours = filterBehaviours((List<PipelineBehaviour<?, ?>>) orderedBehaviours, requestType);

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
        Injector current = injector;
        while (current != null) {
            List<Key<?>> keys = new ArrayList<>(current.getBindings().keySet()); // getBindings() = own level only
            for (Key<?> key : keys) {
                Class<?> rawType = key.getTypeLiteral().getRawType();
                if (RequestHandler.class.isAssignableFrom(rawType) && !rawType.isInterface()) {
                    allHandlers.add((RequestHandler) current.getInstance(key));
                }
            }
            current = current.getParent();
        }
        return allHandlers;
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        TypeToken<?> typeToken = TypeToken.of(GuiceProxyUtils.resolveTargetClass(behaviour));
        TypeToken<?> superType = typeToken.getSupertype((Class) PipelineBehaviour.class);
        Class<?> genericRequestType = superType.resolveType(PipelineBehaviour.class.getTypeParameters()[0]).getRawType();

        return genericRequestType.isAssignableFrom(requestType);
    }
}
