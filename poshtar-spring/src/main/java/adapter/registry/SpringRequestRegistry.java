package adapter.registry;

import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.request.registry.AbstractRequestRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.*;


public class SpringRequestRegistry extends AbstractRequestRegistry implements ApplicationListener<ContextRefreshedEvent> {
    private final ApplicationContext context;

    public SpringRequestRegistry(ApplicationContext context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    private void init(ApplicationContext context) {
        @SuppressWarnings("rawtypes") Map<String, IRequestHandler> allHandlers = context.getBeansOfType(IRequestHandler.class);
        List<IPipelineBehaviour<?, ?>> allBehaviours =
                new ArrayList<>((Collection<IPipelineBehaviour<?, ?>>) (Collection<?>)
                        context.getBeansOfType(IPipelineBehaviour.class).values()
                );
        AnnotationAwareOrderComparator.sort(allBehaviours);

        for (IRequestHandler<?, ?> handler : allHandlers.values()) {
            Class<?> requestType = ResolvableType.forClass(handler.getClass())
                    .as(IRequestHandler.class)
                    .getGeneric(0).resolve();
            if (requestType == null) continue;

            List<IPipelineBehaviour<?, ?>> filteredBehaviours = filterBehaviours(allBehaviours, requestType);
            register(requestType, handler, filteredBehaviours);
        }

    }

    @Override
    protected boolean supportsRequest(IPipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        ResolvableType behaviourInterface = ResolvableType.forClass(behaviour.getClass())
                .as(IPipelineBehaviour.class);
        Class<?> genericRequestType = behaviourInterface.getGeneric(0).resolve();
        if (genericRequestType == null) {
            return behaviourInterface.getGeneric(0).isAssignableFrom(requestType);
        }
        return genericRequestType.isAssignableFrom(requestType);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == context) {
            init(context);
        }
    }
}
