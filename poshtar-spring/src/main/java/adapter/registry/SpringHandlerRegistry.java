package adapter.registry;

import org.example.core.request.IRequest;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.request.registry.IRequestRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.Map;


public class SpringHandlerRegistry implements IRequestRegistry {
    private final ApplicationContext context;
    private final Map<Class<?>,String> handlerBeans = new HashMap<>();

    public SpringHandlerRegistry(ApplicationContext context) {
        this.context = context;
        String[] beanNames = context.getBeanNamesForType(IRequestHandler.class);
        for (String beanName: beanNames){
            Class<?> targetType = context.getType(beanName);
            if (targetType == null) continue;

            ResolvableType resolvableType = ResolvableType.forClass(targetType)
                    .as(IRequestHandler.class);
            Class<?> requestType = resolvableType.getGeneric(0).resolve();
            if(requestType != null)
                handlerBeans.put(requestType, beanName);
        }
    }

    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> void register(Class<TRequest> aClass, IRequestHandler<TRequest, TResponse> iRequestHandler) {

    }

    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> IRequestHandler<TRequest, TResponse> resolve(Class<TRequest> aClass) {
        String beanName = handlerBeans.get(aClass);
        if(beanName == null)
            throw new IllegalArgumentException("No handler registered for request: " + aClass.getName());
        return context.getBean(beanName, IRequestHandler.class);
    }
}
