package poshtar.tests.pipeline.deps.transactional.mandatory.success;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public class SucceedForMandatoryRequest implements IRequest<Unit> {
    public int payload = 0;
}
