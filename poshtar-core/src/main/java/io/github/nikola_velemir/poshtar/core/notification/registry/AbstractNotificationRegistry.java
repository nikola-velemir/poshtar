package io.github.nikola_velemir.poshtar.core.notification.registry;

import io.github.nikola_velemir.poshtar.core.exceptions.AmbiguousHandlerException;
import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class implementing {@link NotificationRegistry}, modeling standard behavior of registering and resolving notification types.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public abstract class AbstractNotificationRegistry implements NotificationRegistry {
    /**
     * Map containing notification to group of handlers mapping, which are later searched for handling a notification.
     */
    private final Map<Class<?>, List<NotificationHandler<?>>> handlerMappings = new HashMap<>();

    /**
     * Implementation of the {@link NotificationRegistry}'s {@code resolve} method.
     * Inserts a handler into list registered for given notification type.
     *
     * @param notificationClass Class literal of the notification to register
     * @param handler           Handler that will handler a notification.
     * @param <TNotification>   Type of notification.
     */
    @Override
    public <TNotification extends Notification> void register(Class<TNotification> notificationClass, NotificationHandler<TNotification> handler) {
        handlerMappings.computeIfAbsent(notificationClass, k -> new ArrayList<>())
                .add(handler);
    }

    /**
     * Implementation of the {@link NotificationRegistry}'s {@code register} method.
     * Returns an empty list if not handlers are found for given notification type.
     *
     * @param type            Class literal of the notification to resolve.
     * @param <TNotification> Type of notification.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <TNotification extends Notification> List<NotificationHandler> resolve(Class<TNotification> type) {
        return (List) handlerMappings.getOrDefault(type, List.of());
    }
}
