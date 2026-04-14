package org.nikola.velemir.poshtar.spring.adapter.request.deps.infrastructure.primitives;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;
import java.util.Map;

@Handler
public class CollectionPrimitiveTypeRequestHandler implements RequestHandler<CollectionPrimitiveTypeRequest, List<Map<Integer,String>>> {
    @Override
    public List<Map<Integer, String>> handle(CollectionPrimitiveTypeRequest request) {
        return null;
    }
}
