package org.example.core.notification.handler;

import org.example.core.notification.INotification;

public interface INotificationHandler<TNotification extends INotification> {
    void handle(TNotification notification);

}
