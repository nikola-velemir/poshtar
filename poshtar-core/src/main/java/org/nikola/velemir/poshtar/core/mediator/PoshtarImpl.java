package org.nikola.velemir.poshtar.core.mediator;

import org.nikola.velemir.poshtar.core.exceptions.AggregateNotificationException;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.nikola.velemir.poshtar.core.request.registry.RequestRegistry;
import org.nikola.velemir.poshtar.core.notification.registry.NotificationRegistry;
import org.nikola.velemir.poshtar.core.notification.Notification;
import org.nikola.velemir.poshtar.core.request.Request;

import java.util.ArrayList;
import java.util.List;

public final class PoshtarImpl implements Poshtar {
    private final RequestRegistry requestRegistry;
    private final NotificationRegistry notificationRegistry;

    public PoshtarImpl(RequestRegistry registry, NotificationRegistry notificationRegistry) {
        this.requestRegistry = registry;
        this.notificationRegistry = notificationRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TReq extends Request<TRes>, TRes> TRes send(TReq request) {

        if(request == null)
            throw new IllegalArgumentException("Request cannot be null");
        var requestChain = requestRegistry.resolve((Class<TReq>) request.getClass());
        return requestChain.execute(request);

    }

    @SuppressWarnings("unchecked")
    @Override
    public <TNotification extends Notification> void publish(TNotification notification) {
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
    private static <TNotification extends Notification> void dispatchNotifications(TNotification notification, List<NotificationHandler> handlers, List<Throwable> collectedErrors) {
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
