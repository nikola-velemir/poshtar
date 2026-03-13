package poshtar.tests.pipeline.deps.transactional.mandatory.fail;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@PipelineBehaviour
public class FailMandatoryPipeline implements IPipelineBehaviour<FailMandatoryRequest, Unit> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Unit handle(FailMandatoryRequest request, RequestDelegate<FailMandatoryRequest, Unit> requestDelegate) {
        System.out.println("Shouldn't call this");
        request.payload += 1;
        return requestDelegate.handle(request);
    }
}
