package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.global;

import org.junit.jupiter.api.Order;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.request.Request;

@Behaviour
public class GlobalTestPipeline<TRequest extends Request<TResponse>, TResponse>
        implements PipelineBehaviour<TRequest, TResponse> {

    @Override
    public TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> requestDelegate) {
        System.out.println("Global pipeline called");
        return requestDelegate.handle(request);
    }
}
