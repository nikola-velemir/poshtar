package org.nikola.velemir.poshtar.spring.adapter.request.deps.transactional.mandatory;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Handler
public class MandatoryRequestHandler implements RequestHandler<MandatoryRequest,String> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public String handle(MandatoryRequest mandatoryRequest) {
        return "";
    }
}
