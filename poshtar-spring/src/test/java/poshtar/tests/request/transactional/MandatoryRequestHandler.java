package poshtar.tests.request.transactional;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
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
