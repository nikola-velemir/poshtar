package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.mediator;

import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.mediator.PoshtarBase;
import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Quarkus-specific implementation of the PoshtaR.
 *
 * <p>
 * This class extends {@link PoshtarBase}, in order to support async notification dispatching.
 * </p>
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class QuarkusPoshtarImpl extends PoshtarBase {
    /**
     * Constructs a new instance of the Quarkus implementation from the registries.
     *
     * @param requestRegistry      designated request registry
     * @param notificationRegistry designated notification registry
     */
    public QuarkusPoshtarImpl(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry) {
        super(requestRegistry, notificationRegistry);
    }

    /**
     * Dispatches an incoming notification concurrently across all matching registered handlers.
     *
     * <p>
     * Every individual handler invocation runs isolated inside an async thread barrier.
     * If one or more handlers fail during execution, the routine tracks their trace exceptions,
     * lets independent routines finish safely, and bundles failures into an aggregate runtime exception.
     * </p>
     *
     * @param <TNotification> the type of the target notification
     * @param notification    the concrete notification
     * @param handlers        the list of matching notification handlers resolved for specific notification
     * @throws AggregateNotificationException if any registered handler throws an unhandled exception during processing
     */
    @Override
    protected <TNotification extends Notification> void dispatch(
            TNotification notification,
            List<NotificationHandler> handlers) {

        List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        List<CompletableFuture<Void>> futures = createFutures(notification, handlers, errors);

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        if (!errors.isEmpty()) {
            throw new AggregateNotificationException(errors);
        }
    }
    @Nonnull
    private static <TNotification extends Notification> List<CompletableFuture<Void>> createFutures(TNotification notification, List<NotificationHandler> handlers, List<Throwable> errors) {
        return handlers.stream()
                .map(handler -> CompletableFuture.runAsync(() -> {
                    try {
                        handler.handle(notification);
                    } catch (Exception e) {
                        errors.add(e);
                        System.err.println("Handler [" + handler.getClass().getSimpleName() + "] failed, continuing...");
                    }
                }))
                .toList();
    }
}
