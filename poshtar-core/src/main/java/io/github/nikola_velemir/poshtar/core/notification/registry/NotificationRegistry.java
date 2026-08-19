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

package io.github.nikola_velemir.poshtar.core.notification.registry;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

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
