package org.nikola.velemir.poshtar.guice.adapter.notification.deps.injection;

import jakarta.inject.Inject;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;


@Handler
public class InjectionNotificationSecondHandler implements NotificationHandler<InjectionNotification> {
    private final DummyIncrementService incrementService;

    @Inject
    public InjectionNotificationSecondHandler(DummyIncrementService incrementService) {
        this.incrementService = incrementService;
    }

    @Override
    public void handle(InjectionNotification injectionNotification) {
        injectionNotification.value = incrementService.inc(injectionNotification.value);
    }
}
