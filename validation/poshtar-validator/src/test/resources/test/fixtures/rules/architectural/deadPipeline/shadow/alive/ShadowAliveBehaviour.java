package io.github.nikola_velemir.poshtar.validator.rules.architectural.deadPipeline.shadow.alive;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class ShadowAliveBehaviour implements PipelineBehaviour<ShadowAliveRequest, Unit> {

    @Override
    public Unit handle(ShadowAliveRequest request, RequestDelegate<ShadowAliveRequest, Unit> next) {
        return shadowDelegate(request, next);
    }

    private Unit shadowDelegate(ShadowAliveRequest request, RequestDelegate<ShadowAliveRequest, Unit> elgato) {
        return elgato.handle(request);
    }
}
