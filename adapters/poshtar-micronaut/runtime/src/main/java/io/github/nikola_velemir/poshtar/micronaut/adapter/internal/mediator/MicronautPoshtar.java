package io.github.nikola_velemir.poshtar.micronaut.adapter.internal.mediator;

import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.mediator.PoshtarBase;
import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

public class MicronautPoshtar extends PoshtarBase {
    private final ExecutorService executorService;

    /**
     * Instantiates a new {@link PoshtarBase} object, with provided {@link RequestRegistry} and {@link NotificationRegistry}.
     *
     * @param requestRegistry      provided request registry, holding all request to behavior-handler mappings.
     * @param notificationRegistry provided request registry, holding all notification to handler set mappings.
     * @param executorService      Micronaut-managed executor used to dispatch notification handlers concurrently.
     */
    public MicronautPoshtar(
            RequestRegistry requestRegistry,
            NotificationRegistry notificationRegistry,
            @Named("poshtar") ExecutorService executorService
    ) {
        super(requestRegistry, notificationRegistry);
        this.executorService = executorService;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected <TNotification extends Notification> void dispatch(TNotification notification, List<NotificationHandler> handlers) {
        List<Throwable> collectedErrors = new CopyOnWriteArrayList<>();

        List<CompletableFuture<Void>> futures = handlers.stream()
                .map(handler -> CompletableFuture.runAsync(() -> {
                    try {
                        handler.handle(notification);
                    } catch (Exception e) {
                        collectedErrors.add(e);
                        System.err.println("Handler [" + handler.getClass().getSimpleName() + "] failed, continuing...");
                    }
                }, executorService))
                .toList();

        // block until all handlers finish, so callers keep today's synchronous contract
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        if (!collectedErrors.isEmpty())
            throw new AggregateNotificationException(new ArrayList<>(collectedErrors));
    }
}
