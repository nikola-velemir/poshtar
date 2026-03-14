package org.example.impl;

import org.example.core.exceptions.AggregateNotificationException;
import org.example.core.mediator.IPoshtar;
import org.example.core.notification.handler.INotificationHandler;
import org.example.core.request.registry.IRequestRegistry;
import org.example.core.notification.registry.INotificationRegistry;
import org.example.core.notification.INotification;
import org.example.core.request.IRequest;

import java.util.ArrayList;
import java.util.List;

public class Poshtar implements IPoshtar {
    private final IRequestRegistry requestRegistry;
    private final INotificationRegistry notificationRegistry;

    public Poshtar(IRequestRegistry registry, INotificationRegistry notificationRegistry) {
        this.requestRegistry = registry;
        this.notificationRegistry = notificationRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TReq extends IRequest<TRes>, TRes> TRes send(TReq request) {

        if(request == null)
            throw new IllegalArgumentException("Request cannot be null");
        var requestChain = requestRegistry.resolve((Class<TReq>) request.getClass());
        return requestChain.execute(request);

    }

    @SuppressWarnings("unchecked")
    @Override
    public <TNotification extends INotification> void publish(TNotification notification) {
        if(notification == null)
            throw new IllegalArgumentException("Request cannot be null");
        var handlers = notificationRegistry.resolve((Class<TNotification>) notification.getClass());
        List<Throwable> collectedErrors = new ArrayList<>();
        if (handlers != null) {
            dispatchNotifications(notification, handlers, collectedErrors);
        }
        if (!collectedErrors.isEmpty())
            throw new AggregateNotificationException(collectedErrors);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <TNotification extends INotification> void dispatchNotifications(TNotification notification, List<INotificationHandler> handlers, List<Throwable> collectedErrors) {
        for (var handler : handlers) {
            try {
                handler.handle(notification);

            } catch (Exception e) {
                collectedErrors.add(e);
                System.err.println("Handler [" + handler.getClass().getSimpleName() + "] failed, continuing...");
            }
        }
    }
}
