package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order;


import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public final class OrderRequest implements Request<Unit> {
    public int payload = 0;
}
