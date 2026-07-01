package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.mock;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class MockFirstNotificationHandler implements NotificationHandler<MockNotification> {
    @Autowired
    private final MockService service;

    public MockFirstNotificationHandler(MockService service) {
        this.service = service;
    }

    @Override
    public void handle(MockNotification mockNotification) {
        mockNotification.setPayload(service.getHello());
    }
}
