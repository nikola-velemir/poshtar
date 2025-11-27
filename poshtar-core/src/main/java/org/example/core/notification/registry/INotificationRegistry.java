package org.example.core.notification.registry;

import org.example.core.notification.INotification;
import org.example.core.notification.handler.INotificationHandler;

import java.util.List;

public interface INotificationRegistry {
    <TNotification extends INotification> void register(Class<TNotification> requestCLass, INotificationHandler<TNotification> requestHandler);
    <TNotification extends INotification> List<INotificationHandler> resolve(Class<TNotification> requestType);

}
