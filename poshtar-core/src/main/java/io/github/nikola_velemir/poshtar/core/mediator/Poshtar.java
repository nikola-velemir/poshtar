package io.github.nikola_velemir.poshtar.core.mediator;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.request.Request;

/**
 * The central for dispatching requests and publishing notifications.
 * <p>
 * Interface acts as a mediator. It supports two primary communication patterns:
 * </p>
 * <ul>
 *     <li><b>Request/Response:</b> A single request is sent to a single registered handler,
 *     returning a specific response.</li>
 *     <li><b>Publish/Subscribe:</b> A single notification is broadcast to zero or more
 *     registered subscribers.</li>
 * </ul>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface Poshtar {
    /**
     * Dispatches a request to its corresponding handler through the pipeline.
     *
     * @param <TRequest>  The type of the request being sent.
     * @param <TResponse> The type of the expected response.
     * @param request     The request object to be processed.
     * @return The response produced by the handler and its associated pipeline.
     * @throws RuntimeException if no handler is registered for the given request type.
     */
    <TRequest extends Request<TResponse>, TResponse> TResponse send(TRequest request);
    /**
     * Broadcasts a notification to all registered handlers.
     * <p>
     * Multiple handlers can process the same notification. If no handlers are registered,
     * the notification is ignored.
     * </p>
     *
     * @param <TNotification> The type of notification being published.
     * @param notification    The notification object to be broadcasted.
     */
    <TNotification extends Notification> void publish(TNotification notification);
}
