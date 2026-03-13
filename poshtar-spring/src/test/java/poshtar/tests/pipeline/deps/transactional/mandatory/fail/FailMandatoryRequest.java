package poshtar.tests.pipeline.deps.transactional.mandatory.fail;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public class FailMandatoryRequest implements IRequest<Unit> {
    public int payload = 0;
}
