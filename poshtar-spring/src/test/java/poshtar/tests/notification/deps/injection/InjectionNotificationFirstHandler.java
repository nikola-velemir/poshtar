package poshtar.tests.notification.deps.injection;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class InjectionNotificationFirstHandler implements NotificationHandler<InjectionNotification> {
    @Autowired
    private final DummyIncrementService incrementService;

    public InjectionNotificationFirstHandler(DummyIncrementService incrementService) {
        this.incrementService = incrementService;
    }

    @Override
    public void handle(InjectionNotification injectionNotification) {
        injectionNotification.value = incrementService.inc(injectionNotification.value);
    }
}
