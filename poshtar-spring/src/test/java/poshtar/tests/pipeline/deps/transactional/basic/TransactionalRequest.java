package poshtar.tests.pipeline.deps.transactional.basic;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public class TransactionalRequest implements Request<Unit> {
    public int payload = 0;
}
