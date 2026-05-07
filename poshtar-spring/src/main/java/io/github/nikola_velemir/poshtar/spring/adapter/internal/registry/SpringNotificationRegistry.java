package io.github.nikola_velemir.poshtar.spring.adapter.internal.registry;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;

import java.util.Map;
/**
 * Class maps specific notification type to its designated handler class, including Spring proxies.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class SpringNotificationRegistry extends AbstractNotificationRegistry implements ApplicationListener<ContextRefreshedEvent> {

    private final ApplicationContext context;
    /**
     * Instantiates the registry, with the provided Spring context.
     * @param context Spring context, used for Posthar component discovery.
     */
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

    /**
     * Method is called upon context refresh to initialize the registry mappings.
     *
     * @param event Spring context refresh event.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == context) {
            init(context);
        }
    }
}
