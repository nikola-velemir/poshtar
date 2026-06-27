package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@ApplicationScoped
public class QuarkusNotificationRegistry extends AbstractNotificationRegistry {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerFromClass(Class<?> beanClass, BeanManager bm) {
        Bean<?> bean = bm.getBeans(beanClass).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No CDI bean for notification handler: " + beanClass.getName()));
        NotificationHandler handler = (NotificationHandler) bm.getReference(
                bean, beanClass, bm.createCreationalContext(bean));

        Class<?> notificationType = extractNotificationType(beanClass);
        if (notificationType == null) return;

        register((Class) notificationType, handler);
        System.out.println("[PoshtaR] Registered notification handler: " + beanClass.getSimpleName());
    }

    private Class<?> extractNotificationType(Class<?> clazz) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Type iface : current.getGenericInterfaces()) {
                if (!(iface instanceof ParameterizedType pt)) continue;
                if (!pt.getRawType().toString().contains(NotificationHandler.class.getSimpleName())) continue;
                Type arg = pt.getActualTypeArguments()[0];
                if (arg instanceof Class<?> notifClass && Notification.class.isAssignableFrom(notifClass)) {
                    return notifClass;
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }
}