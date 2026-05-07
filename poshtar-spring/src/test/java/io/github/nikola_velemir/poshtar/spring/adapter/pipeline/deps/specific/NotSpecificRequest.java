package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.specific;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public final class NotSpecificRequest implements Request<Unit>{
    public int payload = 0;
}