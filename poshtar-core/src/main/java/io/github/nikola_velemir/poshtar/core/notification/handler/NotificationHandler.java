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
