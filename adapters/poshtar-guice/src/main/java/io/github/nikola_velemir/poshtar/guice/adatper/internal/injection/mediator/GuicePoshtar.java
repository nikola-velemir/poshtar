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
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        List<Throwable> collectedErrors = Collections.synchronizedList(new ArrayList<>());
        for (var handler : handlers) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    handler.handle(notification);
                } catch (Exception e) {
                    collectedErrors.add(e);
                    System.err.println("Handler [" + handler.getClass().getSimpleName() + "] failed, continuing...");
                }
            });

            futures.add(future);
        }
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            // runAsync catches exceptions internally, but if join() itself fails, we catch it here.
        }

        // 3. Now that everything is joined, check if any async handler threw an error
        if (!collectedErrors.isEmpty()) {
            throw new AggregateNotificationException(collectedErrors);
        }

    }
}
