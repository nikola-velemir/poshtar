package org.example.impl;

import org.example.core.mediator.IMediator;
import org.example.core.request.registry.IRequestRegistry;
import org.example.core.notification.registry.INotificationRegistry;
import org.example.core.notification.INotification;
import org.example.core.request.IRequest;

public class Mediator implements IMediator {
    private final IRequestRegistry requestRegistry;
    private final INotificationRegistry notificationRegistry;

    public Mediator(IRequestRegistry registry, INotificationRegistry notificationRegistry) {
        this.requestRegistry = registry;
        this.notificationRegistry = notificationRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> TResponse send(TRequest tRequest) {


        var requestChain = requestRegistry.resolve((Class<TRequest>) tRequest.getClass());
        return requestChain.execute(tRequest);

    }

    @SuppressWarnings("unchecked")
    @Override
    public <TNotification extends INotification> void publish(TNotification notification) {
        var handlers = notificationRegistry.resolve((Class<TNotification>) notification.getClass());
        if (handlers != null) {
            for (var handler : handlers) {
                handler.handle(notification);
            }
        }
    }
}
