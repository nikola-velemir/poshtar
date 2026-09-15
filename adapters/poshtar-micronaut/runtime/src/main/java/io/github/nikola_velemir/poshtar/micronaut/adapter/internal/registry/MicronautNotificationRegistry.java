package io.github.nikola_velemir.poshtar.micronaut.adapter.internal.registry;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import io.micronaut.context.BeanContext;
import io.micronaut.inject.BeanDefinition;

import java.util.Collection;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MicronautNotificationRegistry extends AbstractNotificationRegistry {
    public MicronautNotificationRegistry(BeanContext context) {
        Collection<BeanDefinition<NotificationHandler>> definitions =
                context.getBeanDefinitions(NotificationHandler.class);

        for (BeanDefinition<NotificationHandler> definition : definitions) {
            Class<?> notificationType = definition.getTypeArguments(NotificationHandler.class)
                    .get(0)
                    .getType();

            if (Notification.class.isAssignableFrom(notificationType)) {
                @SuppressWarnings("unchecked")

                Class<? extends Notification> castedType = (Class<? extends Notification>) notificationType;
                NotificationHandler handler = context.getBean(definition);

                register(castedType, handler);
            }
        }
    }
}
