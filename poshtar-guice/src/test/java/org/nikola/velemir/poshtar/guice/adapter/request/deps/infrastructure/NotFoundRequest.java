package org.nikola.velemir.poshtar.guice.adapter.request.deps.infrastructure;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.opt.api.annotations.request.SuppressOrphan;

@SuppressOrphan
public final class NotFoundRequest implements Request<Unit> {
}
