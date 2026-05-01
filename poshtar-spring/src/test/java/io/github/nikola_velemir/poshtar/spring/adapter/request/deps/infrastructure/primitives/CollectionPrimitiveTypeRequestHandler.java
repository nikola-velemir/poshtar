package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.primitives;



import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;
import java.util.Map;

@Handler
public class CollectionPrimitiveTypeRequestHandler implements RequestHandler<CollectionPrimitiveTypeRequest, List<Map<Integer,String>>> {
    @Override
    public List<Map<Integer, String>> handle(CollectionPrimitiveTypeRequest request) {
        return null;
    }
}
