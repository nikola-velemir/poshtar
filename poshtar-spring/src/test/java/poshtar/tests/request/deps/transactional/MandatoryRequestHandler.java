package poshtar.tests.request.deps.transactional;

import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequestHandler
public class MandatoryRequestHandler implements IRequestHandler<MandatoryRequest,String> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public String handle(MandatoryRequest mandatoryRequest) {
        return "";
    }
}
