package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception.BeanNotFoundException;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

@ApplicationScoped
public class QuarkusNotificationRegistry extends AbstractNotificationRegistry {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerFromClass(Class<?> handlerClass, Class<?> notifClass, BeanManager bm) {
        Bean<?> bean = bm.getBeans(handlerClass).stream().findFirst()
                .orElseThrow(() -> new BeanNotFoundException(handlerClass));
        NotificationHandler handler = (NotificationHandler) bm.getReference(
                bean, handlerClass, bm.createCreationalContext(bean));

        register((Class) notifClass, handler);

    }
}