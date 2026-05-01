package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.singleResponsibility;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class SingleResponsibilityHandler
        implements RequestHandler<SingleResponsibilityFirstRequest, Unit>
//        ,
//        BehaviourMaskingInterface
{
//
//    @Override
//    public Unit handle(SingleResponsibilityFirstRequest request, RequestDelegate<SingleResponsibilityFirstRequest, Unit> delegate) {
//        return null;
//    }

    @Override
    public Unit handle(SingleResponsibilityFirstRequest request) {
        return null;
    }
}
