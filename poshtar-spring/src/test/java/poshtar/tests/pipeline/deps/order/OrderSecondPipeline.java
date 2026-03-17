package poshtar.tests.pipeline.deps.order;

import org.junit.jupiter.api.Order;
import org.nikola.velemir.poshtar.core.annotations.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PipelineBehaviour
@Order(2)
public class OrderSecondPipeline implements IPipelineBehaviour<OrderRequest, Unit> {
    @Override
    public Unit handle(OrderRequest request, RequestDelegate<OrderRequest, Unit> requestDelegate) {
        assertEquals(1, request.payload);
        request.payload += 1;
        return requestDelegate.handle(request);
    }
}
