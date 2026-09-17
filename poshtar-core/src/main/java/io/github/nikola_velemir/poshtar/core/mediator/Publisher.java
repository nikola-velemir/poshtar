package io.github.nikola_velemir.poshtar.core.mediator;

import io.github.nikola_velemir.poshtar.core.notification.Notification;

public interface Publisher {
    /**
     * Broadcasts a notification to all registered handlers.
     * <p>
     * Multiple handlers can process the same notification. If no handlers are registered,
     * the notification is ignored.
     * </p>
     *
     * @param <TNotification> The type of notification being published.
     * @param notification    The notification object to be broadcasted.
     */
    <TNotification extends Notification> void publish(TNotification notification);
}
