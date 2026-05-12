/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.registry;

import com.google.common.reflect.TypeToken;
import com.google.inject.Binding;
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
@SuppressWarnings({"rawtypes", "unchecked"})

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
                    TypeToken<?> typeToken = TypeToken.of(GuiceProxyUtils.resolveTargetClass(handler));
                    TypeToken<?> superType = typeToken.getSupertype((Class) NotificationHandler.class);
                    Class<?> notificationType = superType.resolveType(NotificationHandler.class.getTypeParameters()[0]).getRawType();
                    if (Notification.class.isAssignableFrom(notificationType)) {
                        Class<? extends Notification> castedType = (Class<? extends Notification>) notificationType;
                        register(castedType, handler);
                    }
                }
            }
            current = current.getParent();
        }
    }
}
