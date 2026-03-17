package poshtar.tests.pipeline.deps.specific;


import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.types.Unit;

public class NotSpecificRequest implements IRequest<Unit> {
    public int payload = 0;
}