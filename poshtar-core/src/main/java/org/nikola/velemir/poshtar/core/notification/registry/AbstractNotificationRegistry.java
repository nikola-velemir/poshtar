package org.nikola.velemir.poshtar.core.notification.registry;

import org.nikola.velemir.poshtar.core.notification.Notification;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractNotificationRegistry implements NotificationRegistry {
    private final Map<Class<?>, List<NotificationHandler<?>>> handlerMappings = new HashMap<>();

    @Override
    public <TNotification extends Notification> void register(Class<TNotification> notificationClass, NotificationHandler<TNotification> handler) {
        handlerMappings.computeIfAbsent(notificationClass, k -> new ArrayList<>())
                .add(handler);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <TNotification extends Notification> List<NotificationHandler> resolve(Class<TNotification> type) {
        return (List) handlerMappings.getOrDefault(type, List.of());
    }
}
