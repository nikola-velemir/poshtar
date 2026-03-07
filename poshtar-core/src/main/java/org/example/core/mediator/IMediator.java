package org.example.core.mediator;

import org.example.core.notification.INotification;
import org.example.core.request.IRequest;

public interface IMediator {
    <TRequest extends IRequest<TResponse>,TResponse> TResponse send(TRequest request) throws Exception;
    <TNotification extends INotification> void publish(TNotification notification);
}
