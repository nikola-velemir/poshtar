package poshtar.tests.pipeline.deps.transactional;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public class TransactionalRequest implements IRequest<Unit> {
    public int payload = 0;
}
