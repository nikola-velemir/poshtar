package poshtar.tests.pipeline.deps.dead;

import org.nikola.velemir.poshtar.core.annotations.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;

@PipelineBehaviour
public class DeadPipeline implements IPipelineBehaviour<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request, RequestDelegate<DeadRequest, Unit> requestDelegate) {
        System.out.println("Called dead pipeline!");
        return null;
    }
}
