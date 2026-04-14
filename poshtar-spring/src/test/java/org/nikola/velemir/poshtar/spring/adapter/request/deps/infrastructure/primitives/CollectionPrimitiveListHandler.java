package org.nikola.velemir.poshtar.spring.adapter.request.deps.infrastructure.primitives;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

@Handler
public class CollectionPrimitiveListHandler implements RequestHandler<CollectionPrimitiveListRequest, List<CollectionPrimitiveListResponse>> {
    @Override
    public List<CollectionPrimitiveListResponse> handle(CollectionPrimitiveListRequest request) {
        return List.of();
    }
}
