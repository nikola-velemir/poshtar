package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.async;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;

@Handler
public class FailForAsyncFirstHandler implements NotificationHandler<FailForAsyncNotification> {
    @Override
    public void handle(FailForAsyncNotification failForAsyncNotification) {
        System.out.println("First handler called!");

    }
}
