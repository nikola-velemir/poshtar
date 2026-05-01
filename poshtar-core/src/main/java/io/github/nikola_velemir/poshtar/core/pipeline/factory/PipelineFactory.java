package io.github.nikola_velemir.poshtar.core.pipeline.factory;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

public class PipelineFactory {
    @SuppressWarnings("unchecked")
    public static <TRequest extends Request<TResponse>, TResponse> RequestInvocationChain<TRequest, TResponse> create(RequestHandler<?, ?> rawHandler, List<PipelineBehaviour<?, ?>> rawBehaviours) {
        RequestHandler<TRequest, TResponse> handler =
                (RequestHandler<TRequest, TResponse>) rawHandler;

        List<PipelineBehaviour<TRequest, TResponse>> behaviours = rawBehaviours.stream()
                .map(b -> (PipelineBehaviour<TRequest, TResponse>) b)
                .toList();
        RequestDelegate<TRequest, TResponse> nextNode = (request) -> handler.handle(request);
        for (int i = behaviours.size() - 1; i >= 0; i--) {
            nextNode = createNextNode(nextNode, behaviours.get(i));
        }
        final RequestDelegate<TRequest, TResponse> head = nextNode;
        return (request) -> head.handle(request);
    }

    public static <TRequest extends Request<TResponse>, TResponse> RequestDelegate<TRequest, TResponse> createNextNode(RequestDelegate<TRequest, TResponse> nextNode, PipelineBehaviour<TRequest, TResponse> behaviour) {
        return (request) -> behaviour.handle(request, nextNode);
    }
}
