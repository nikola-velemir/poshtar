package org.nikola.velemir.poshtar.guice.adapter.request.deps.nullRequest;

import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.opt.api.annotations.request.SuppressUnregistered;

@SuppressUnregistered
public class NullRequest implements Request<Unit> {
}
