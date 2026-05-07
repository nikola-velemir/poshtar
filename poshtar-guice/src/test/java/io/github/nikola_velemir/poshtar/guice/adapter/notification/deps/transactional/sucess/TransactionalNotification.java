package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.transactional.sucess;


import io.github.nikola_velemir.poshtar.core.notification.Notification;

public record TransactionalNotification(String payload) implements Notification {
}
