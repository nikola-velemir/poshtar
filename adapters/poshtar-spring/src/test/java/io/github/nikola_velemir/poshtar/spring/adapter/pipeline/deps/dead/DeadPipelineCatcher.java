package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.dead;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.validator.api.annotations.pipeline.SuppressDead;

@SuppressDead
@Behaviour
public class DeadPipelineCatcher implements PipelineBehaviour<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request, RequestDelegate<DeadRequest, Unit> requestDelegate) {
        System.out.println("Called dead pipeline!");
        return null;
    }
}