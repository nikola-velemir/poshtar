package io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.mock;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import jakarta.inject.Inject;

@Handler
public class MockFirstNotificationHandler implements NotificationHandler<MockNotification> {
    @Inject
    MockService service;

    public MockFirstNotificationHandler(MockService service) {
        this.service = service;
    }

    @Override
    public void handle(MockNotification mockNotification) {
        mockNotification.setPayload(service.getHello());
    }
}
