package poshtar.tests.pipeline.deps.order;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;
import org.junit.jupiter.api.Order;

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
