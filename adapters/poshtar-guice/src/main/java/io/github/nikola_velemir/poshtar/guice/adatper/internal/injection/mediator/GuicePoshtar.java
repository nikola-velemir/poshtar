package io.github.nikola_velemir.poshtar.guice.adatper.internal.injection.mediator;

import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.mediator.PoshtarBase;
import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GuicePoshtar extends PoshtarBase {
    public GuicePoshtar(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry) {
        super(requestRegistry, notificationRegistry);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <TNotification extends Notification> void dispatch(TNotification notification, List<NotificationHandler> handlers) {
        List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        List<CompletableFuture<Void>> futures = handlers.stream()
                .map(handler -> CompletableFuture.runAsync(() -> {
                    try {
                        handler.handle(notification);
                    } catch (Exception e) {
                        errors.add(e);
                        System.err.println("Handler [" + handler.getClass().getSimpleName() + "] failed, continuing...");
                    }
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        if (!errors.isEmpty()) {
            throw new AggregateNotificationException(errors);
        }
    }

}
