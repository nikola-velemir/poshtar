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
