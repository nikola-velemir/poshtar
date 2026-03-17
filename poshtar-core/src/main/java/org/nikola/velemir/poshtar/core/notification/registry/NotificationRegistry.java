package org.nikola.velemir.poshtar.core.notification.registry;

import org.nikola.velemir.poshtar.core.notification.Notification;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;

import java.util.List;

public interface NotificationRegistry {
    <TNotification extends Notification> void register(Class<TNotification> notificationClass, NotificationHandler<TNotification> handler);
    @SuppressWarnings("rawtypes")
    <TNotification extends Notification> List<NotificationHandler> resolve(Class<TNotification> type);
}
