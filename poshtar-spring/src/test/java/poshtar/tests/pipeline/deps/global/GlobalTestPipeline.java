package poshtar.tests.pipeline.deps.global;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.request.IRequest;
import org.junit.jupiter.api.Order;

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
