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

package io.github.nikola_velemir.poshtar.core.notification.handler;

import io.github.nikola_velemir.poshtar.core.notification.Notification;

/**
 * Defines a component responsible for processing a specific type of {@link NotificationHandler}.
 * <p>
 * Each implementation of this interface is bound to a single notification type and
 * is responsible for executing the core logic that it contains.
 * Handlers are typically invoked by the {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}
 * mediator after the notifications is passed.
 * </p>
 *
 * @param <TNotification>>  The specific type of notification this handler processes.
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface NotificationHandler<TNotification extends Notification> {
    /**
     * Handles the given notification.
     *
     * @param notification The notification object containing the input data.
     */
    void handle(TNotification notification);

}
