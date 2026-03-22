package org.nikola.velemir.poshtar.guice.adapter.notification.deps.transactional.fail;


import org.nikola.velemir.poshtar.core.notification.Notification;

public record FailTransactionalNotification(String payload) implements Notification {
}
