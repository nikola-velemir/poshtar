package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.mock;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class MockHierarchyNotificationHandler implements NotificationHandler<MockHierarchyNotification> {
    @Autowired
    private final MockService service;

    public MockHierarchyNotificationHandler(MockService service) {
        this.service = service;
    }

    @Override
    public void handle(MockHierarchyNotification mockNotification) {
        mockNotification.setPayload(service.getHi());
    }
}
