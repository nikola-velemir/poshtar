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

package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.recorder;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception.BeanNotFoundException;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import java.util.List;
import java.util.Map;

/**
 * Internal package-private utility responsible for extracting operational proxies for pipeline behaviours from the active CDI container.
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
final class ProxyUtility {
    private ProxyUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Resolves and extracts proxy references for all provided pipeline behaviour classes
     * from the active CDI container.
     *
     * @param orderedBehaviourClasses the user-defined ordered list of behaviour class keys
     * @param bm                      the active CDI {@link BeanManager} used to resolve beans and contextual references
     * @return map pairing behaviour class names to their fully realized CDI proxy instances
     * @throws RuntimeException if a declared pipeline behaviour class cannot be resolved as an active CDI bean
     */
    @Nonnull
    static Map<String, PipelineBehaviour<?, ?>> extractBehaviourProxies(List<Class<? extends PipelineBehaviour<?, ?>>> orderedBehaviourClasses, BeanManager bm) {
        Map<String, PipelineBehaviour<?, ?>> behaviourProxies = new java.util.LinkedHashMap<>();
        for (Class<?> clazz : orderedBehaviourClasses) {
            Bean<?> bean = bm.resolve(bm.getBeans(clazz));
            if (bean == null) throw new BeanNotFoundException(clazz);
            behaviourProxies.put(clazz.getName(), (PipelineBehaviour<?, ?>) bm.getReference(
                    bean, clazz, bm.createCreationalContext(bean)));
        }
        return behaviourProxies;
    }

}
