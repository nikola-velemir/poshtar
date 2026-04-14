package org.nikola.velemir.poshtar.spring.adapter.request.deps.infrastructure.notfound;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.opt.api.annotations.request.SuppressOrphan;

@SuppressOrphan
public final class NotFoundRequest implements Request<Unit> {
}
