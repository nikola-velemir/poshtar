package poshtar.tests.pipeline.deps.specific;

import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public class SpecificRequest implements Request<Unit> {
    public int payload = 0;
}
