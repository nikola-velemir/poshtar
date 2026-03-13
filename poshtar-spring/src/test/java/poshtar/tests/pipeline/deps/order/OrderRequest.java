package poshtar.tests.pipeline.deps.order;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public class OrderRequest implements IRequest<Unit> {
    public int payload = 0;
}
