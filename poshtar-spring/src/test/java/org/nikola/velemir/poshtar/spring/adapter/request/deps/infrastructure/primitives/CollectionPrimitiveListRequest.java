package org.nikola.velemir.poshtar.spring.adapter.request.deps.infrastructure.primitives;

import org.nikola.velemir.poshtar.core.request.Request;

import java.util.List;

public record CollectionPrimitiveListRequest() implements Request<List<CollectionPrimitiveListResponse>> {
}
