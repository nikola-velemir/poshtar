package poshtar.tests.pipeline.deps.specific;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;

@PipelineBehaviour
public class SpecificPipeline implements IPipelineBehaviour<SpecificRequest, Unit> {
    @Override
    public Unit handle(SpecificRequest request, RequestDelegate<SpecificRequest, Unit> requestDelegate) {
        request.payload += 1;
        var outputRequest = requestDelegate.handle(request);
        System.out.println("Called specific pipeline");
        return outputRequest;
    }
}
