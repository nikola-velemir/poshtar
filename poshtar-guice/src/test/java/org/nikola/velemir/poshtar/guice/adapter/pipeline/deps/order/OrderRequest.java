package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.order;

import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public final class OrderRequest implements Request<Unit> {
    public int payload = 0;
}
