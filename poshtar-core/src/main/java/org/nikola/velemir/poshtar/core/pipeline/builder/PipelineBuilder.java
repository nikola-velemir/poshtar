package org.nikola.velemir.poshtar.core.pipeline.builder;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.request.RequestInvocationChain;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

public class PipelineBuilder {
    @SuppressWarnings("unchecked")
    public static <TRequest extends Request<TResponse>, TResponse> RequestInvocationChain<TRequest, TResponse> build(RequestHandler<?, ?> rawHandler, List<PipelineBehaviour<?, ?>> rawBehaviours) {
        RequestHandler<TRequest, TResponse> handler =
                (RequestHandler<TRequest, TResponse>) rawHandler;

        List<PipelineBehaviour<TRequest, TResponse>> behaviours = rawBehaviours.stream()
                .map(b -> (PipelineBehaviour<TRequest, TResponse>) b)
                .toList();
        RequestDelegate<TRequest, TResponse> nextNode = (request) -> handler.handle(request);
        for (int i = behaviours.size() - 1; i >= 0; i--) {
            nextNode = buildNextNode(nextNode, behaviours.get(i));
        }
        final RequestDelegate<TRequest, TResponse> head = nextNode;
        return (request) -> head.handle(request);
    }

    public static <TRequest extends Request<TResponse>, TResponse> RequestDelegate<TRequest, TResponse> buildNextNode(RequestDelegate<TRequest, TResponse> nextNode, PipelineBehaviour<TRequest, TResponse> behaviour) {
        return (request) -> behaviour.handle(request, nextNode);
    }
}
