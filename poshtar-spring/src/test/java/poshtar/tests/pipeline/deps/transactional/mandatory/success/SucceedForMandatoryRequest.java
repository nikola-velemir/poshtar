package poshtar.tests.pipeline.deps.transactional.mandatory.success;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public class SucceedForMandatoryRequest implements Request<Unit> {
    public int payload = 0;
}
