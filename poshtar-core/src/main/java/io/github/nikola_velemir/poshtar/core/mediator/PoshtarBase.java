/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.core.mediator;

import io.github.nikola_velemir.poshtar.core.exceptions.AggregateNotificationException;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.request.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link Poshtar} interface. Class is the core logic to dispatching requests and notifications, routing them to designated handlers or behaviors.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public abstract class PoshtarBase implements Poshtar {

    /**
     * Request registry that is accessed to retrieve a pipeline chain for a given request type.
     */
    private final RequestRegistry requestRegistry;
    /**
     * Notificatio registry that is accessed to retrieve a set of handlers that will handle a notification type.
     */
    private final NotificationRegistry notificationRegistry;

    /**
     * Instantiates a new {@link PoshtarBase} object, with provided {@link RequestRegistry} and {@link NotificationRegistry}.
     *
     * @param requestRegistry      provided request registry, holding all request to behavior-handler mappings.
     * @param notificationRegistry provided request registry, holding all notification to handler set mappings.
     */
    public PoshtarBase(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry) {
        this.requestRegistry = requestRegistry;
        this.notificationRegistry = notificationRegistry;
    }

    /**
     * Receives a request, dispatching to the corresponding handler through the request pipeline.
     *
     * @param request The request object to be processed.
     * @param <TReq>  Type of the request to be handled.
     * @param <TRes>  Type of the response that will be returned.
     * @return Object of {@param <TRes>}, once the request has been handled.
     * @throws IllegalArgumentException if passed request is null.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <TReq extends Request<TRes>, TRes> TRes send(TReq request) {

        if (request == null)
            throw new IllegalArgumentException("Request cannot be null");
        var requestChain = requestRegistry.resolve((Class<TReq>) request.getClass());
        return requestChain.execute(request);

    }

    protected NotificationRegistry getNotificationRegistry() {
        return this.notificationRegistry;
    }
    /**
     * Receives a notification, dispatching it to the corresponding handlers.
     *
     * @param notification    The notification object to be broadcasted.
     * @param <TNotification> Type of the notification to be handled.
     * @throws IllegalArgumentException       if passed notification is null.
     * @throws AggregateNotificationException if any notification handlers failed during their execution, dumping all aggregated exceptions.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <TNotification extends Notification> void publish(TNotification notification) {
        if (notification == null)
            throw new IllegalArgumentException("Request cannot be null");
        var handlers = notificationRegistry.resolve((Class<TNotification>) notification.getClass());
        if (handlers != null) {
            this.dispatch(notification, handlers);
        }

    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <TNotification extends Notification> void dispatch(TNotification notification, List<NotificationHandler> handlers) {
        List<Throwable> collectedErrors = new ArrayList<>();

        for (var handler : handlers) {
            try {
                handler.handle(notification);

            } catch (Exception e) {
                collectedErrors.add(e);
                System.err.println("Handler [" + handler.getClass().getSimpleName() + "] failed, continuing...");
            }
        }
        if (!collectedErrors.isEmpty())
            throw new AggregateNotificationException(collectedErrors);

    }
}
