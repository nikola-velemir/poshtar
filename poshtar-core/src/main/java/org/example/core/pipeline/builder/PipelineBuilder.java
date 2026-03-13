package org.example.core.pipeline.builder;

import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.request.IRequest;
import org.example.core.request.RequestInvocationChain;
import org.example.core.request.handler.IRequestHandler;

import java.util.List;

public class PipelineBuilder {
    @SuppressWarnings("unchecked")
    public static <TRequest extends IRequest<TResponse>, TResponse> RequestInvocationChain<TRequest, TResponse> build(IRequestHandler<?, ?> rawHandler, List<IPipelineBehaviour<?, ?>> rawBehaviours) {
        IRequestHandler<TRequest, TResponse> handler =
                (IRequestHandler<TRequest, TResponse>) rawHandler;

        List<IPipelineBehaviour<TRequest, TResponse>> behaviours = rawBehaviours.stream()
                .map(b -> (IPipelineBehaviour<TRequest, TResponse>) b)
                .toList();
        RequestDelegate<TRequest, TResponse> nextNode = (request) -> handler.handle(request);
        for (int i = behaviours.size() - 1; i >= 0; i--) {
            nextNode = buildNextNode(nextNode, behaviours.get(i));
        }
        final RequestDelegate<TRequest, TResponse> head = nextNode;
        return (request) -> head.handle(request);
    }

    public static <TRequest extends IRequest<TResponse>, TResponse> RequestDelegate<TRequest, TResponse> buildNextNode(RequestDelegate<TRequest, TResponse> nextNode, IPipelineBehaviour<TRequest, TResponse> behaviour) {
        return (request) -> behaviour.handle(request, nextNode);
    }
}
