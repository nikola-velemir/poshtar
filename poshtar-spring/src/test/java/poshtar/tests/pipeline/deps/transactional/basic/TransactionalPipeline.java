package poshtar.tests.pipeline.deps.transactional.basic;
import org.nikola.velemir.poshtar.core.annotations.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Transactional;

@PipelineBehaviour
public class TransactionalPipeline implements IPipelineBehaviour<TransactionalRequest, Unit> {
    @Override
    @Transactional
    public Unit handle(TransactionalRequest request, RequestDelegate<TransactionalRequest, Unit> requestDelegate) {
        request.payload += 1;
        var res = requestDelegate.handle(request);
        System.out.println("Called transactional behaviour");
        return res;
    }
}
