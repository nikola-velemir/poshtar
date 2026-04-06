package nikola.velemir.poshtar.spring.adapter.internal.registry;

import org.nikola.velemir.poshtar.core.notification.Notification;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.nikola.velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;

import java.util.Map;

public class SpringNotificationRegistry extends AbstractNotificationRegistry implements ApplicationListener<ContextRefreshedEvent> {

    private final ApplicationContext context;

    public SpringNotificationRegistry(ApplicationContext context) {
        this.context = context;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void init(ApplicationContext context) {
        Map<String, NotificationHandler> allHandlers  = context.getBeansOfType(NotificationHandler.class);

        for (NotificationHandler<Notification> handler : allHandlers.values()) {


            ResolvableType resolvableType = ResolvableType.forClass(handler.getClass())
                    .as(NotificationHandler.class);

            Class<?> notificationType = resolvableType.getGeneric(0).resolve();

            if (notificationType != null && Notification.class.isAssignableFrom(notificationType)) {
                @SuppressWarnings("unchecked")
                Class<? extends Notification> castedType = (Class<? extends Notification>) notificationType;
                register(castedType, (NotificationHandler) handler);
            }

        }
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == context) {
            init(context);
        }
    }
}
