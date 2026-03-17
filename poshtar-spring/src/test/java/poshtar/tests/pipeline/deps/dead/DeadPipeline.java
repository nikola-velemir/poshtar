package poshtar.tests.pipeline.deps.dead;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;

@Behaviour
public class DeadPipeline implements PipelineBehaviour<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request, RequestDelegate<DeadRequest, Unit> requestDelegate) {
        System.out.println("Called dead pipeline!");
        return null;
    }
}
