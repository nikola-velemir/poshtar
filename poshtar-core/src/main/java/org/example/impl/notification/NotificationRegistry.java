package org.example.impl.notification;

import org.example.core.notification.handler.INotificationHandler;
import org.example.core.notification.registry.INotificationRegistry;
import org.example.core.notification.INotification;

import java.util.*;

public class NotificationRegistry implements INotificationRegistry {
    private final Map<Class<?>, List<INotificationHandler>> handlers = new HashMap<>();
    @Override
    public <TNotification extends INotification> void register(Class<TNotification> requestCLass, INotificationHandler<TNotification> requestHandler) {
        var foundHandlerList = handlers.get(requestCLass);
        if (foundHandlerList == null) {
            handlers.put(requestCLass, new ArrayList<>(List.of(requestHandler)));
            return;
        }
        foundHandlerList.add(requestHandler);
    }

    @Override
    public <TNotification extends INotification> List<INotificationHandler> resolve(Class<TNotification> requestType) {
        return handlers.get(requestType);
    }
}
