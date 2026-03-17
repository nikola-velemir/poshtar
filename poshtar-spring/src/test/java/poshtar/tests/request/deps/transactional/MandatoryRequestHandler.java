package poshtar.tests.request.deps.transactional;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Behaviour
public class MandatoryRequestHandler implements RequestHandler<MandatoryRequest,String> {
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public String handle(MandatoryRequest mandatoryRequest) {
        return "";
    }
}
