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

package io.github.nikola_velemir.poshtar.core.notification;

/**
 * Defines a notification that can be dispatched to the mediator.
 * <p>
 * Class implementing this interface represents a notification that can be handled by one or multiple handlers.
 * {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}'s {@code publish} method processes this notification.
 * </p>
 * <p>Notifications are considered "fire-and-forget".</p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface Notification {
}
