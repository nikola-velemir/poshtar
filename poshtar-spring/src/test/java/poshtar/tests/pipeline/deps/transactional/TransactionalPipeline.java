package poshtar.tests.pipeline.deps.transactional;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;
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
