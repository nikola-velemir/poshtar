package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.order;



import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Handler
public class OrderRequestHandler implements RequestHandler<OrderRequest, Unit> {
    @Override
    public Unit handle(OrderRequest request) {
        assertEquals(2,request.payload);
        request.payload += 1;
        return Unit.Value;
    }
}
