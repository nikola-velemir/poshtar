package org.nikola.velemir.poshtar.guice.adapter.notification.deps.transactional.sucess;


import org.nikola.velemir.poshtar.core.notification.Notification;

public record TransactionalNotification(String payload) implements Notification {
}
