package org.nikola.velemir.poshtar.core.notification.registry;

import org.nikola.velemir.poshtar.core.notification.INotification;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractNotificationRegistry implements INotificationRegistry{
    private final Map<Class<?>, List<INotificationHandler<?>>> handlerMappings = new HashMap<>();

    @Override
    public <TNotification extends INotification> void register(Class<TNotification> notificationClass, INotificationHandler<TNotification> handler) {
        handlerMappings.computeIfAbsent(notificationClass, k -> new ArrayList<>())
                .add(handler);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <TNotification extends INotification> List<INotificationHandler> resolve(Class<TNotification> type) {
        return (List) handlerMappings.getOrDefault(type, List.of());
    }
}
