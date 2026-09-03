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

/**
 * Quarkus-specific implementation of the request registry that integrates
 * request processing with the active CDI container.
 *
 * <p>
 * This class translates build-time discovered handler metadata into proxy-managed,
 * CDI bean references and binds them with pipeline behaviours.
 * </p>
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
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