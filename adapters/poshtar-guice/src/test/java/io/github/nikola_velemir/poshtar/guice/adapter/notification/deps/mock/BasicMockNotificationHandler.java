package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.mock;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import jakarta.inject.Inject;

@Handler
public class BasicMockNotificationHandler implements NotificationHandler<MockNotification> {
    MockService service;
    @Inject

    public BasicMockNotificationHandler(MockService service) {
        this.service = service;
    }

    @Override
    public void handle(MockNotification mockNotification) {
        mockNotification.setPayload(service.getHello());
    }
}
