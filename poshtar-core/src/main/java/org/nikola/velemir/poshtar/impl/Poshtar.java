package org.nikola.velemir.poshtar.impl;

import org.nikola.velemir.poshtar.core.exceptions.AggregateNotificationException;
import org.nikola.velemir.poshtar.core.mediator.IPoshtar;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;
import org.nikola.velemir.poshtar.core.request.registry.IRequestRegistry;
import org.nikola.velemir.poshtar.core.notification.registry.INotificationRegistry;
import org.nikola.velemir.poshtar.core.notification.INotification;
import org.nikola.velemir.poshtar.core.request.IRequest;

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
