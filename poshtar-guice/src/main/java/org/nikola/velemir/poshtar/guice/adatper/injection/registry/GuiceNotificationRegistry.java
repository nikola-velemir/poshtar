package org.nikola.velemir.poshtar.guice.adatper.injection.registry;

import com.google.common.reflect.TypeToken;
import com.google.inject.Binding;
import com.google.inject.Injector;
import org.nikola.velemir.poshtar.core.notification.Notification;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GuiceNotificationRegistry extends AbstractNotificationRegistry {
    public GuiceNotificationRegistry(Injector injector) {
        init(injector);
    }

    private void init(Injector injector) {
        for (Binding<?> binding : injector.getAllBindings().values()) {
            Class<?> rawType = binding.getKey().getTypeLiteral().getRawType();
            if (NotificationHandler.class.isAssignableFrom(rawType) && !rawType.isInterface()) {
                NotificationHandler handler = (NotificationHandler) injector.getInstance(binding.getKey());
                TypeToken<?> typeToken = TypeToken.of(handler.getClass());
                TypeToken<?> superType = typeToken.getSupertype((Class) NotificationHandler.class);
                Class<?> notificationType = superType.resolveType(NotificationHandler.class.getTypeParameters()[0]).getRawType();
                if (Notification.class.isAssignableFrom(notificationType)) {
                    Class<? extends Notification> castedType = (Class<? extends Notification>) notificationType;
                    register(castedType, handler);
                }
            }
        }
    }
}
