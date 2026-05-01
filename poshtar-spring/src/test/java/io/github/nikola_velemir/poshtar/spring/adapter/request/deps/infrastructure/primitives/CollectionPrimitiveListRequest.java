package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.primitives;

import io.github.nikola_velemir.poshtar.core.request.Request;
import java.util.List;

public record CollectionPrimitiveListRequest() implements Request<List<CollectionPrimitiveListResponse>>{}