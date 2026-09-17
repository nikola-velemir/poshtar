package io.github.nikola_velemir.poshtar.validator.rules.architectural.deadPipeline.supressed;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.validator.api.annotations.pipeline.SuppressDead;

@Behaviour
@SuppressDead
public class SuppressedBehaviour implements PipelineBehaviour<SuppressedRequest, Unit> {
    @Override
    public Unit handle(SuppressedRequest request, RequestDelegate<SuppressedRequest, Unit> next) {
        return null;
    }
}
