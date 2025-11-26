package org.example.impl.request;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IVoidRequestHandler;
import org.example.core.types.Unit;

@RequestHandler
public class OtherBasicHandler implements IVoidRequestHandler<OtherRequest> {


    @Override
    public Unit handle(OtherRequest request) {
        return Unit.Value;
    }
}
