package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;


public record FailTransactionalRequest(String payload) implements Request<Unit> {
}
