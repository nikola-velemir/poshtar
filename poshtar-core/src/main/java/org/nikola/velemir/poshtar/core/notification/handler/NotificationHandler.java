package org.nikola.velemir.poshtar.core.notification.handler;

import org.nikola.velemir.poshtar.core.notification.Notification;

public interface NotificationHandler<TNotification extends Notification> {
    void handle(TNotification notification);

}
