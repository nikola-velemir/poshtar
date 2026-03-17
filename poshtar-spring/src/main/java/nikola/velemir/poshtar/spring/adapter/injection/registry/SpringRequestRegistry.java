package nikola.velemir.poshtar.spring.adapter.injection.registry;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.request.registry.AbstractRequestRegistry;
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
        @SuppressWarnings("rawtypes") Map<String, RequestHandler> allHandlers = context.getBeansOfType(RequestHandler.class);
        List<PipelineBehaviour<?, ?>> allBehaviours =
                new ArrayList<>((Collection<PipelineBehaviour<?, ?>>) (Collection<?>)
                        context.getBeansOfType(PipelineBehaviour.class).values()
                );
        AnnotationAwareOrderComparator.sort(allBehaviours);

        for (RequestHandler<?, ?> handler : allHandlers.values()) {
            Class<?> requestType = ResolvableType.forClass(handler.getClass())
                    .as(RequestHandler.class)
                    .getGeneric(0).resolve();
            if (requestType == null || !Request.class.isAssignableFrom(requestType)) {
                continue;
            }

            List<PipelineBehaviour<?, ?>> filteredBehaviours = filterBehaviours(allBehaviours, requestType);

            Class<Request<Object>> castedRequest = (Class<Request<Object>>) requestType;
            RequestHandler<Request<Object>, Object> castedHandler = (RequestHandler<Request<Object>, Object>) handler;

            register(castedRequest, castedHandler, filteredBehaviours);
        }

    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        ResolvableType behaviourInterface = ResolvableType.forClass(behaviour.getClass())
                .as(PipelineBehaviour.class);
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
