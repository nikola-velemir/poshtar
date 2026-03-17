package org.nikola.velemir.poshtar.core.notification.handler;

import org.nikola.velemir.poshtar.core.notification.INotification;

public interface INotificationHandler<TNotification extends INotification> {
    void handle(TNotification notification);

}
