package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.fail;


import io.github.nikola_velemir.poshtar.core.notification.Notification;

public record FailTransactionalNotification(String payload) implements Notification {
}
