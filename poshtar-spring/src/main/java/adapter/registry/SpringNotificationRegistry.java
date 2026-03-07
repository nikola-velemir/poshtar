package adapter;

import org.example.core.notification.INotification;
import org.example.core.notification.handler.INotificationHandler;
import org.example.core.notification.registry.INotificationRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringNotificationRegistry implements INotificationRegistry {

    private final ApplicationContext context;
    private final Map<Class<?>, List<String>> handlerMap = new HashMap<>();

    public SpringNotificationRegistry(ApplicationContext context) {
        this.context = context;
        init();
    }

    private void init(){
        String[] beanNames = context.getBeanNamesForType(INotificationHandler.class);

        for(String beanName:beanNames){
            Class<?> targetType = context.getType(beanName);
            if (targetType == null) continue;

            ResolvableType resolvableType = ResolvableType.forClass(targetType)
                    .as(INotificationHandler.class);

            Class<?> notificationType = resolvableType.getGeneric(0).resolve();

            if (notificationType != null) {
                handlerMap.computeIfAbsent(notificationType, k -> new ArrayList<>())
                        .add(beanName);
            }

        }
    }

    @Override
    public <TNotification extends INotification> void register(Class<TNotification> aClass, INotificationHandler<TNotification> iNotificationHandler) {

    }

    @Override
    public <TNotification extends INotification> List<INotificationHandler> resolve(Class<TNotification> aClass) {
        return List.of();
    }
}
