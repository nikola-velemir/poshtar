package org.nikola.velemir.poshtar.core.notification.registry;

import org.nikola.velemir.poshtar.core.notification.INotification;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;

import java.util.List;

public interface INotificationRegistry {
    <TNotification extends INotification> void register(Class<TNotification> notificationClass, INotificationHandler<TNotification> handler);
    @SuppressWarnings("rawtypes")
    <TNotification extends INotification> List<INotificationHandler> resolve(Class<TNotification> type);
}
