package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class QuarkusNotificationRegistry extends AbstractNotificationRegistry {
    @Inject
    @SuppressWarnings("rawtypes")
    Instance<NotificationHandler> handlers;

    @Inject
    BeanManager beanManager;

    /**
     * Called once at startup. Arc has already resolved all @Handler beans,
     * so we just iterate and register — no classpath scanning needed.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    void init(@Observes StartupEvent event) {
        for (NotificationHandler handler : handlers) {
            Bean<?> bean = beanManager.resolve(
                    beanManager.getBeans(handler.getClass())
            );

            Type notificationHandlerType = bean.getTypes().stream()
                    .filter(t -> t instanceof ParameterizedType pt
                            && pt.getRawType().equals(NotificationHandler.class))
                    .findFirst()
                    .orElse(null);

            if (notificationHandlerType instanceof ParameterizedType pt) {
                Type arg = pt.getActualTypeArguments()[0];
                if (arg instanceof Class<?> notificationType
                        && Notification.class.isAssignableFrom(notificationType)) {
                    register(
                            (Class<Notification>) notificationType,
                            (NotificationHandler<Notification>) handler
                    );
                }
            }
        }
    }
}