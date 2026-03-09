package poshtar.tests.notification.deps.injection;

import org.example.core.annotations.NotificationHandler;
import org.example.core.notification.handler.INotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;

@NotificationHandler

public class InjectionNotificationSecondHandler implements INotificationHandler<InjectionNotification> {
    @Autowired
    private final DummyIncrementService incrementService;

    public InjectionNotificationSecondHandler(DummyIncrementService incrementService) {
        this.incrementService = incrementService;
    }

    @Override
    public void handle(InjectionNotification injectionNotification) {
        injectionNotification.value = incrementService.inc(injectionNotification.value);
    }
}
