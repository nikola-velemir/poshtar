package poshtar.tests.pipeline.deps.transactional.basic;


import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.types.Unit;

public class TransactionalRequest implements IRequest<Unit> {
    public int payload = 0;
}
