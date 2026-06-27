package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
@ApplicationScoped
public class QuarkusNotificationRegistry extends AbstractNotificationRegistry {


    @Inject
    BeanManager beanManager;

    /**
     * Called once at startup. Arc has already resolved all @Handler beans,
     * so we just iterate and register — no classpath scanning needed.
     */
    void init(@Observes StartupEvent event) {
        Set<Bean<?>> beans = beanManager.getBeans(Object.class, Any.Literal.INSTANCE);
        for (Bean<?> bean : beans) {
            Class<?> beanClass = bean.getBeanClass();
            System.out.println("Suspected class: " + beanClass.getName());
            if (!implementsInterface(beanClass, NotificationHandler.class.getName())) continue;

            CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
            NotificationHandler handler = (NotificationHandler) beanManager.getReference(bean, beanClass, ctx);
            Class<?> notificationType = extractNotificationType(beanClass);
            if(notificationType == null) continue;

            register((Class) notificationType, handler);
            System.out.println("[PoshtaR] Registered notification handler: " + beanClass.getSimpleName());

        }
    }
    private boolean implementsInterface(Class<?> clazz, String interfaceName) {
        while (clazz != null && clazz != Object.class) {
            for (Class<?> iface : clazz.getInterfaces()) {
                if (iface.getName().equals(interfaceName)) return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private Class<?> extractNotificationType(Class<?> clazz) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Type iface : current.getGenericInterfaces()) {
                if (!(iface instanceof ParameterizedType pt)) continue;
                if (!pt.getRawType().toString().contains(NotificationHandler.class.getSimpleName())) continue;
                Type arg = pt.getActualTypeArguments()[0];
                if (arg instanceof Class<?> notifClass
                        && Notification.class.isAssignableFrom(notifClass)) {
                    return notifClass;
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }
}