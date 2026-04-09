package org.nikola.velemir.poshtar.spring.adapter.request.deps.nullRequest;

import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.opt.api.annotations.request.SuppressUnregistered;

@SuppressUnregistered
public final class NullRequest implements Request<Unit> {
}
