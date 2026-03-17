package poshtar.tests.notification.deps.transactional;

import org.nikola.velemir.poshtar.core.annotations.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@NotificationHandler
public class MandatoryNotificationHandler implements INotificationHandler<MandatoryNotification> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handle(MandatoryNotification mandatoryNotification) {

    }
}
