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

package io.github.nikola_velemir.poshtar.core.notification.registry;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain;

import java.util.List;

/**
 * Interface representing a notification registry, where all notification type to handler type mappings will be stored.
 * <p>
 * Implementations of this interface serve two purposes:
 * </p>
 * <ul>
 *     <li>
 *         <b>Resolution:</b> Resolving the appropriate list of {@link NotificationHandler} for a given request type,
 *         allowing the mediator to execute multiple handlers.
 *     </li>
 *     <li>
 *         <b>Registration:</b> Binding a specific {@link Notification} type to set of {@link NotificationHandler}s.
 *     </li>
 * </ul>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface NotificationRegistry {
    /**
     *  Registers a handler to the specific notification type.
     *
     * @param notificationClass Class literal of the notification to register.
     * @param handler           Handler that will handler a notification.
     * @param <TNotification>   The type of the notification.
     */
    <TNotification extends Notification> void register(Class<TNotification> notificationClass, NotificationHandler<TNotification> handler);

    /**
     * Resolves the list of handlers for a specific notification.
     *
     * @param type             Class literal of the notification to resolve.
     * @param <TNotification>> The type of the notification being handled.
     * @return A list of {@link NotificationHandler} that supports the notification type.
     */
    @SuppressWarnings("rawtypes")
    <TNotification extends Notification> List<NotificationHandler> resolve(Class<TNotification> type);
}
