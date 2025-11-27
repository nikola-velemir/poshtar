package org.example.core.notification.handler;

import org.example.core.types.Unit;
import org.example.core.notification.INotification;

public interface INotificationHandler<TNotification extends INotification> {
    Unit handle(TNotification notification);

}
