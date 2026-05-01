package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.fail;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Behaviour
public class FailMandatoryPipeline implements PipelineBehaviour<FailMandatoryRequest, Unit> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Unit handle(FailMandatoryRequest request, RequestDelegate<FailMandatoryRequest, Unit> requestDelegate) {
        System.out.println("Shouldn't call this");
        request.payload += 1;
        return requestDelegate.handle(request);
    }
}
