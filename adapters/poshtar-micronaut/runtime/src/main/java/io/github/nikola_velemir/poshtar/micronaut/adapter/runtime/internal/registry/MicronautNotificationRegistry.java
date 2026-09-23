package io.github.nikola_velemir.poshtar.micronaut.adapter.runtime.internal.registry;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import io.github.nikola_velemir.poshtar.micronaut.adapter.runtime.internal.discovery.NotificationType;
import io.micronaut.context.BeanContext;
import io.micronaut.inject.BeanDefinition;

import java.util.Collection;

/**
 * Micronaut-specific implementation of {@link AbstractNotificationRegistry}.
 * <p>
 * Scans the Micronaut {@link BeanContext} for all registered
 * {@link NotificationHandler} bean definitions,
 * extracts the target {@link Notification} generic type parameter, and
 * registers each handler in the registry.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class MicronautNotificationRegistry extends AbstractNotificationRegistry {
    /**
     * Constructs the notification registry and eagerly discovers and registers all
     * {@link NotificationHandler} beans present in the Micronaut context.
     *
     * @param context the Micronaut {@link BeanContext} used for handler discovery
     *                and lookup
     */
    public MicronautNotificationRegistry(BeanContext context) {
        Collection<BeanDefinition<NotificationHandler>> definitions = context
                .getBeanDefinitions(NotificationHandler.class);

        for (BeanDefinition<NotificationHandler> definition : definitions) {
            Class<?> notificationType = definition.classValue(NotificationType.class).orElse(null);

            if (notificationType != null && Notification.class.isAssignableFrom(notificationType)) {
                @SuppressWarnings("unchecked")

                Class<? extends Notification> castedType = (Class<? extends Notification>) notificationType;
                NotificationHandler handler = context.getBean(definition);

                register(castedType, handler);
            }
        }
    }
}
