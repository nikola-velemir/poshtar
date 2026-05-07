package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.primitives;



import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

@Handler
public class CollectionPrimitiveListHandler implements RequestHandler<CollectionPrimitiveListRequest, List<CollectionPrimitiveListResponse>> {
    @Override
    public List<CollectionPrimitiveListResponse> handle(CollectionPrimitiveListRequest request) {
        return List.of();
    }
}
