package poshtar.tests.pipeline.deps.global;

import org.junit.jupiter.api.Order;
import org.nikola.velemir.poshtar.core.annotations.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.request.IRequest;

@Order(0)
@PipelineBehaviour
public class GlobalTestPipeline<TRequest extends IRequest<TResponse>, TResponse>
        implements IPipelineBehaviour<TRequest, TResponse> {

    @Override
    public TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> requestDelegate) {
        System.out.println("Global pipeline called");
        return requestDelegate.handle(request);
    }
}
