package io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.mock;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import jakarta.inject.Inject;

@Handler
public class MockHierarchyNotificationHandler implements NotificationHandler<MockHierarchyNotification> {
    @Inject
    MockService service;

    public MockHierarchyNotificationHandler(MockService service) {
        this.service = service;
    }

    @Override
    public void handle(MockHierarchyNotification mockNotification) {
        mockNotification.setPayload(service.getHi());
    }
}
