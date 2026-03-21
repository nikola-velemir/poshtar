package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public class TransactionalRequest implements Request<Unit> {
    public int payload = 0;
}
