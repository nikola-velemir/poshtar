package poshtar.tests.pipeline.deps.specific;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public class SpecificRequest implements IRequest<Unit> {
    public int payload = 0;
}
