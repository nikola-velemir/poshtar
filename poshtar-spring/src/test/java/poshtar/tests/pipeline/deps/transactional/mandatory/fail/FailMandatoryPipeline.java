package poshtar.tests.pipeline.deps.transactional.mandatory.fail;

import org.nikola.velemir.poshtar.core.annotations.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
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
