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
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
/**
 * Quarkus-specific implementation of the notification registry that integrates
 * notification dispatching with the active CDI container.
 *
 * <p>
 * This class handles the runtime assembly of notification routing structures by converting
 * discovered class types into proxy-managed, container-backed bean instances.
 * </p>
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
@ApplicationScoped
public class QuarkusNotificationRegistry extends AbstractNotificationRegistry {
    /**
     * Resolves the CDI bean for a given notification handler class
     * and maps it to its corresponding notification.
     *
     * @param handlerClass the class of the target notification handler
     * @param notifClass the class of the event notification being handled
     * @param bm the active CDI {@link BeanManager} used to query the container and instantiate references
     * @throws BeanNotFoundException if the target handler class cannot be located as a registered CDI bean
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerFromClass(Class<?> handlerClass, Class<?> notifClass, BeanManager bm) {
        Bean<?> bean = bm.getBeans(handlerClass).stream().findFirst()
                .orElseThrow(() -> new BeanNotFoundException(handlerClass));
        NotificationHandler handler = (NotificationHandler) bm.getReference(
                bean, handlerClass, bm.createCreationalContext(bean));

        register((Class) notifClass, handler);

    }
}