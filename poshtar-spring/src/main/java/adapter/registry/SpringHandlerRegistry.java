package adapter.registry;

import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.request.IRequest;
import org.example.core.request.RequestChain;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.request.registry.IRequestRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.*;


public class SpringHandlerRegistry implements IRequestRegistry, ApplicationListener<ContextRefreshedEvent> {
    private final ApplicationContext context;
    private final Map<Class<?>, RequestChain<?, ?>> handlerBeans = new HashMap<>();

    public SpringHandlerRegistry(ApplicationContext context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    private void init(ApplicationContext context) {
        @SuppressWarnings("rawtypes") Map<String, IRequestHandler> allHandlers = context.getBeansOfType(IRequestHandler.class);
        List<IPipelineBehaviour<?, ?>> allBehaviours =
                new ArrayList<>((Collection<IPipelineBehaviour<?, ?>>) (Collection<?>)
                        context.getBeansOfType(IPipelineBehaviour.class).values()
                );
        System.out.println("Svi pronađeni behaviouri: " + allBehaviours.stream().map(b -> b.getClass().getSimpleName()).toList());
        AnnotationAwareOrderComparator.sort(allBehaviours);

        for (IRequestHandler<?, ?> handler : allHandlers.values()) {
            Class<?> requestType = ResolvableType.forClass(handler.getClass())
                    .as(IRequestHandler.class)
                    .getGeneric(0).resolve();
            if (requestType == null) continue;

            List<IPipelineBehaviour<?, ?>> filteredBehaviours = filterBehaviours(allBehaviours, requestType);
            handlerBeans.put(requestType, buildChain(handler, filteredBehaviours));

        }

    }

    private List<IPipelineBehaviour<?, ?>> filterBehaviours(
            List<IPipelineBehaviour<?, ?>> allBehaviours, Class<?> requestType) {

        return allBehaviours.stream()
                .filter(b -> supportsRequest(b, requestType))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private <TRequest extends IRequest<TResponse>, TResponse> RequestChain<TRequest, TResponse> buildChain(
            IRequestHandler<?, ?> rawHandler,
            List<IPipelineBehaviour<?, ?>> rawBehaviours) {

        IRequestHandler<TRequest, TResponse> handler =
                (IRequestHandler<TRequest, TResponse>) rawHandler;

        List<IPipelineBehaviour<TRequest, TResponse>> behaviours = rawBehaviours.stream()
                .map(b -> (IPipelineBehaviour<TRequest, TResponse>) b)
                .toList();

        return (request) -> {
            RequestDelegate<TResponse> next = () -> handler.handle(request);

            for (int i = behaviours.size() - 1; i >= 0; i--) {
                IPipelineBehaviour<TRequest, TResponse> behaviour = behaviours.get(i);
                RequestDelegate<TResponse> currentNext = next;
                next = () -> behaviour.handle(request, currentNext);
            }

            return next.handle();
        };
    }

    private boolean supportsRequest(IPipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        ResolvableType behaviourInterface = ResolvableType.forClass(behaviour.getClass())
                .as(IPipelineBehaviour.class);
        Class<?> genericRequestType = behaviourInterface.getGeneric(0).resolve();
        if (genericRequestType == null) {
            return behaviourInterface.getGeneric(0).isAssignableFrom(requestType);
        }
        return genericRequestType.isAssignableFrom(requestType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> RequestChain<TRequest, TResponse> resolve(Class<TRequest> aClass) {
        RequestChain<TRequest, TResponse> beanName = (RequestChain<TRequest, TResponse>) handlerBeans.get(aClass);
        if (beanName == null)
            throw new IllegalArgumentException("No handler registered for request: " + aClass.getName());
        return beanName;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == context) {
            init(context);
        }
    }
}
