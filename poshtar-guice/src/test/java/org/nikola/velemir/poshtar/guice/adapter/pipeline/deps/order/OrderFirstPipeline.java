package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.order;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Behaviour
public class OrderFirstPipeline implements PipelineBehaviour<OrderRequest, Unit> {

    @Override
    public Unit handle(OrderRequest request, RequestDelegate<OrderRequest, Unit> requestDelegate) {
        System.out.println("Called first pipeline");
        assertEquals(0, request.payload);
        request.payload += 1;
        return requestDelegate.handle(request);
    }
}
