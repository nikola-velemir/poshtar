package org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.fail;


import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public class FailMandatoryRequest implements Request<Unit> {
    public int payload = 0;
}
