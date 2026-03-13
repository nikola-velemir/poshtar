package poshtar.tests.pipeline.deps.dead;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;

@PipelineBehaviour
public class DeadPipeline implements IPipelineBehaviour<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request, RequestDelegate<DeadRequest, Unit> requestDelegate) {
        System.out.println("Called dead pipeline!");
        return null;
    }
}
