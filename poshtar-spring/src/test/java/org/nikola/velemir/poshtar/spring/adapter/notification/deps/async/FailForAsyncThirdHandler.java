package org.nikola.velemir.poshtar.spring.adapter.notification.deps.async;


import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Handler
public class FailForAsyncThirdHandler implements NotificationHandler<FailForAsyncNotification> {
    @Override
    @Async
    @Transactional(propagation = Propagation.MANDATORY)
    public void handle(FailForAsyncNotification failForAsyncNotification) {
        throw new RuntimeException("Purposefully causing exception");
    }
}
