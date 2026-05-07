package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.transactional.mandatory;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Handler
public class MandatoryNotificationHandler implements NotificationHandler<MandatoryNotification> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handle(MandatoryNotification mandatoryNotification) {

    }
}
