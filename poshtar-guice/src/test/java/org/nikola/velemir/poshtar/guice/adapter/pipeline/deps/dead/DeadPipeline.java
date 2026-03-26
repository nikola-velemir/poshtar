package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.dead;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.opt.annotations.suppression.SuppressDead;

@SuppressDead
@Behaviour
public class DeadPipeline implements PipelineBehaviour<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request, RequestDelegate<DeadRequest, Unit> requestDelegate) {
        handleDead(request, requestDelegate);
        return null;
    }

    private static void handleDead(DeadRequest request, RequestDelegate<DeadRequest, Unit> requestDelegate) {
        System.out.println("Called dead pipeline!");
        requestDelegate.handle(request);
    }
}
