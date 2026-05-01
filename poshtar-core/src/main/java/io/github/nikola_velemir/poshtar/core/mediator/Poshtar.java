package io.github.nikola_velemir.poshtar.core.mediator;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.request.Request;

public interface Poshtar {
    <TRequest extends Request<TResponse>,TResponse> TResponse send(TRequest request);
    <TNotification extends Notification> void publish(TNotification notification);
}
