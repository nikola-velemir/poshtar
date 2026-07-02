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
import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Class maps specific notification type to its designated handler class, including proxies.
 *
 * <p>Registry checks injector bindings to resolve type mappings</p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked", "UnstableApiUsage"})

public class GuiceNotificationRegistry extends AbstractNotificationRegistry {
    /**
     * Instantiates the registry, with the provided Guice injector.
     *
     * @param injector Guice injector, used to discover classes thru bindings.
     */
    public GuiceNotificationRegistry(Injector injector) {
        init(injector);
    }

    private void init(Injector injector) {
        Injector current = injector;
        while (current != null) {
            List<Key<?>> keys = new ArrayList<>(current.getBindings().keySet());
            for (Key<?> key : keys) {
                Class<?> rawType = key.getTypeLiteral().getRawType();
                if (NotificationHandler.class.isAssignableFrom(rawType) && !rawType.isInterface()) {
                    NotificationHandler handler = (NotificationHandler) current.getInstance(key);

                    TypeToken<?> typeToken = TypeToken.of(rawType);
                    TypeToken<?> superType = typeToken.getSupertype((Class) NotificationHandler.class);
                    Class<?> notificationType = superType.resolveType(NotificationHandler.class.getTypeParameters()[0]).getRawType();

                    if (Notification.class.isAssignableFrom(notificationType)) {
                        register((Class<? extends Notification>) notificationType, handler);
                    }
                }
            }
            current = current.getParent();
        }
    }
}
