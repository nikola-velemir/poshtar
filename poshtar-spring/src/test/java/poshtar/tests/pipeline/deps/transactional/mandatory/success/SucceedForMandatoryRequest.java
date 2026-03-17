package poshtar.tests.pipeline.deps.transactional.mandatory.success;


import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.types.Unit;

public class SucceedForMandatoryRequest implements IRequest<Unit> {
    public int payload = 0;
}
