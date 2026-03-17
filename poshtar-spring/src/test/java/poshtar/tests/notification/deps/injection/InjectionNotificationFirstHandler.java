package poshtar.tests.notification.deps.injection;

import org.nikola.velemir.poshtar.core.annotations.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.handler.INotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;

@NotificationHandler
public class InjectionNotificationFirstHandler implements INotificationHandler<InjectionNotification> {
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
