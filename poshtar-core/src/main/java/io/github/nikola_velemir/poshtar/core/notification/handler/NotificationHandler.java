package io.github.nikola_velemir.poshtar.core.notification.handler;

import io.github.nikola_velemir.poshtar.core.notification.Notification;

public interface NotificationHandler<TNotification extends Notification> {
    void handle(TNotification notification);

}
