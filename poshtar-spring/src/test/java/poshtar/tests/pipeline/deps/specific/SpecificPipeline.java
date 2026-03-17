package poshtar.tests.pipeline.deps.specific;


import org.nikola.velemir.poshtar.core.annotations.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;

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
