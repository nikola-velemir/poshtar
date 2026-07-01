package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.mock;

import io.github.nikola_velemir.poshtar.core.notification.Notification;

public final class MockHierarchyNotification implements Notification {
    private String payload = "";

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MockHierarchyNotification that = (MockHierarchyNotification) o;
        return payload.equals(that.payload);
    }

    @Override
    public int hashCode() {
        return payload.hashCode();
    }
}
