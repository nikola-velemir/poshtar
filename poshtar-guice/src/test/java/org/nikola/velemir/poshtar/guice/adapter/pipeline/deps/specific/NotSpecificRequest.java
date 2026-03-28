package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.specific;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public final class NotSpecificRequest implements Request<Unit> {
    public int payload = 0;
}