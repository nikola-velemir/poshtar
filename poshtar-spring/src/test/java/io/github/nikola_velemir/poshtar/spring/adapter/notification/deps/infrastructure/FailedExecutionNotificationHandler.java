package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.infrastructure;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Handler
public class FailedExecutionNotificationHandler implements NotificationHandler<FailedExecutionNotification> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handle(FailedExecutionNotification failedExecutionNotification) {
    }
}
