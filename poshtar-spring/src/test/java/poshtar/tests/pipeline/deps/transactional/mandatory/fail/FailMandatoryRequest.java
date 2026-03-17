package poshtar.tests.pipeline.deps.transactional.mandatory.fail;


import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.types.Unit;

public class FailMandatoryRequest implements IRequest<Unit> {
    public int payload = 0;
}
