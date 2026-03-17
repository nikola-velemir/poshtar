package org.nikola.velemir.poshtar.core.mediator;

import org.nikola.velemir.poshtar.core.notification.Notification;
import org.nikola.velemir.poshtar.core.request.Request;

public interface Poshtar {
    <TRequest extends Request<TResponse>,TResponse> TResponse send(TRequest request);
    <TNotification extends Notification> void publish(TNotification notification);
}
