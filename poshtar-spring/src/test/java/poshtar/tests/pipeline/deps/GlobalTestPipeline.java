package poshtar.tests.pipeline.deps;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.request.IRequest;

@PipelineBehaviour
public class GlobalTestPipeline<TRequest extends IRequest<TResponse>, TResponse>
        implements IPipelineBehaviour<TRequest, TResponse> {    @Override
    public TResponse handle(TRequest tRequest, RequestDelegate<TRequest, TResponse> requestDelegate) {

            return

    }
}
