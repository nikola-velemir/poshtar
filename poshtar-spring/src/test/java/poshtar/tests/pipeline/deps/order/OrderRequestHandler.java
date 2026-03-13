package poshtar.tests.pipeline.deps.order;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequestHandler
public class OrderRequestHandler implements IRequestHandler<OrderRequest, Unit> {
    @Override
    public Unit handle(OrderRequest request) {
        assertEquals(2,request.payload);
        request.payload += 1;
        return Unit.Value;
    }
}
