package org.example.impl.request;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;

@RequestHandler
public class BasicRequestHandler implements IRequestHandler<BasicRequest, BasicResponse> {
    @Override
    public BasicResponse handle(BasicRequest request) {
        return new BasicResponse("HALLOO");
    }
}

