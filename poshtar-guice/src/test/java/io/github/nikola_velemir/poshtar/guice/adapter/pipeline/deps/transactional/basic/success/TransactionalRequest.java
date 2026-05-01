package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public final class TransactionalRequest implements Request<Unit> {
    public int payload = 0;
}
