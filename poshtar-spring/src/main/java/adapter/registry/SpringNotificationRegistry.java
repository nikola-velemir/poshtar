package adapter.registry;

import org.example.core.notification.INotification;
import org.example.core.notification.handler.INotificationHandler;
import org.example.core.notification.registry.AbstractNotificationRegistry;
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
        Map<String, INotificationHandler> allHandlers  = context.getBeansOfType(INotificationHandler.class);

        for (INotificationHandler<INotification> handler : allHandlers.values()) {


            ResolvableType resolvableType = ResolvableType.forClass(handler.getClass())
                    .as(INotificationHandler.class);

            Class<?> notificationType = resolvableType.getGeneric(0).resolve();

            if (notificationType != null && INotification.class.isAssignableFrom(notificationType)) {
                @SuppressWarnings("unchecked")
                Class<? extends INotification> castedType = (Class<? extends INotification>) notificationType;
                register(castedType, (INotificationHandler) handler);
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
