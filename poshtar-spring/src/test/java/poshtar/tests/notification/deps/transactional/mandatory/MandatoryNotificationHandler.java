package poshtar.tests.notification.deps.transactional.mandatory;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Handler
public class MandatoryNotificationHandler implements NotificationHandler<MandatoryNotification> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handle(MandatoryNotification mandatoryNotification) {

    }
}
