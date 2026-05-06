package io.github.nikola_velemir.poshtar.core.notification.registry;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain;

import java.util.List;

/**
 * Interface representing a notification registry, where all notification type to handler type mappings will be stored.
 * <p>
 * Implementations of this interface serve two purposes:
 * </p>
 * <ul>
 *     <li>
 *         <b>Resolution:</b> Resolving the appropriate list of {@link NotificationHandler} for a given request type,
 *         allowing the mediator to execute multiple handlers.
 *     </li>
 *     <li>
 *         <b>Registration:</b> Binding a specific {@link Notification} type to set of {@link NotificationHandler}s.
 *     </li>
 * </ul>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface NotificationRegistry {
    /**
     *
     * @param notificationClass Class literal of the notification to register
     * @param handler           Handler that will handler a notification.
     * @param <TNotification>   The type of the notification.
     */
    <TNotification extends Notification> void register(Class<TNotification> notificationClass, NotificationHandler<TNotification> handler);

    /**
     * Resolves the list of handlers for a specific notification.
     *
     * @param type             Class literal of the notification to resolve.
     * @param <TNotification>> The type of the notification being handled.
     * @return A list of {@link NotificationHandler} that supports the notification type.
     */
    @SuppressWarnings("rawtypes")
    <TNotification extends Notification> List<NotificationHandler> resolve(Class<TNotification> type);
}
