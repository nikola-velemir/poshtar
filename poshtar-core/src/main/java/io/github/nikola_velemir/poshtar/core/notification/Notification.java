package io.github.nikola_velemir.poshtar.core.notification;

/**
 * Defines a notification that can be dispatched to the mediator.
 * <p>
 * Class implementing this interface represents a notification that can be handled by one or multiple handlers.
 * {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}'s {@code publish} method processes this notification.
 * </p>
 * <p>Notifications are considered "fire-and-forget".</p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface Notification {
}
