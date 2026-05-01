package io.github.nikola_velemir.poshtar.core.notification.registry;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

import java.util.List;

public interface NotificationRegistry {
    <TNotification extends Notification> void register(Class<TNotification> notificationClass, NotificationHandler<TNotification> handler);
    @SuppressWarnings("rawtypes")
    <TNotification extends Notification> List<NotificationHandler> resolve(Class<TNotification> type);
}
