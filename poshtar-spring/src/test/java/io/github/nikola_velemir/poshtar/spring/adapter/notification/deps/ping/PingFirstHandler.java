package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.ping;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class PingFirstHandler implements NotificationHandler<PingNotification> {

    @Override
    public void handle(PingNotification pingNotification) {
        pingNotification.payload += 1;
    }
}
