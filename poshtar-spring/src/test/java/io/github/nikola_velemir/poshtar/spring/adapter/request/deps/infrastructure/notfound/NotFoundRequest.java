package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.notfound;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.opt.api.annotations.request.SuppressOrphan;

@SuppressOrphan
public final class NotFoundRequest implements Request<Unit> {
}
