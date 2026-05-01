package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.specific;


import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class SpecificPipeline implements PipelineBehaviour<SpecificRequest, Unit> {
    @Override
    public Unit handle(SpecificRequest request, RequestDelegate<SpecificRequest, Unit> requestDelegate) {
        request.payload += 1;
        var outputRequest = requestDelegate.handle(request);
        System.out.println("Called specific pipeline");
        return outputRequest;
    }
}
