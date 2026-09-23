package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.hierarchy;

import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.request.Request;

public record HierarchyRequest() implements Query<String> {
}
