package poshtar.tests.pipeline.deps.order;

import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Order(1)
@PipelineBehaviour
public class OrderFirstPipeline implements IPipelineBehaviour<OrderRequest, Unit> {

    @Override
    public Unit handle(OrderRequest request, RequestDelegate<OrderRequest, Unit> requestDelegate) {
        assertEquals(0, request.payload);
        request.payload += 1;
        return requestDelegate.handle(request);
    }
}
