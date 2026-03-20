package org.nikola.velemir.poshtar.spring.adapter.notification.deps.async;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.scheduling.annotation.Async;

@Handler
public class FailForAsyncSecondHandler implements NotificationHandler<FailForAsyncNotification> {
    @Override
    @Async
    public void handle(FailForAsyncNotification failForAsyncNotification) {
        System.out.println("Second handler called!");
    }
}
