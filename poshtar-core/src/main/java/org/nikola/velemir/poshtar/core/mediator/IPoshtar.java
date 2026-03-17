package org.nikola.velemir.poshtar.core.mediator;

import org.nikola.velemir.poshtar.core.notification.INotification;
import org.nikola.velemir.poshtar.core.request.IRequest;

public interface IPoshtar {
    <TRequest extends IRequest<TResponse>,TResponse> TResponse send(TRequest request);
    <TNotification extends INotification> void publish(TNotification notification);
}
