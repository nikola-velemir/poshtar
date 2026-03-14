package org.example.core.mediator;

import org.example.core.notification.INotification;
import org.example.core.request.IRequest;

public interface IPoshtar {
    <TRequest extends IRequest<TResponse>,TResponse> TResponse send(TRequest request);
    <TNotification extends INotification> void publish(TNotification notification);
}
