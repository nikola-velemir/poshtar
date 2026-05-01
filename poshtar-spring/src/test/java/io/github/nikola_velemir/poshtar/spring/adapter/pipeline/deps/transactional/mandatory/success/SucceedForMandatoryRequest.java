package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.success;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public final class SucceedForMandatoryRequest implements Request<Unit> {
    public int payload = 0;
}
