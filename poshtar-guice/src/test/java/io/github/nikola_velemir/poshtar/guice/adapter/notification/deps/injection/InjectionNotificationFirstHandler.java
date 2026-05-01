package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.injection;

import jakarta.inject.Inject;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;


@Handler
public class InjectionNotificationFirstHandler implements NotificationHandler<InjectionNotification> {
    private final DummyIncrementService incrementService;

    @Inject
    public InjectionNotificationFirstHandler(DummyIncrementService incrementService) {
        this.incrementService = incrementService;
    }

    @Override
    public void handle(InjectionNotification injectionNotification) {
        injectionNotification.value = incrementService.inc(injectionNotification.value);
    }
}
