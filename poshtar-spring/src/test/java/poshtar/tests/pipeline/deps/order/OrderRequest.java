package poshtar.tests.pipeline.deps.order;


import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.types.Unit;

public class OrderRequest implements IRequest<Unit> {
    public int payload = 0;
}
