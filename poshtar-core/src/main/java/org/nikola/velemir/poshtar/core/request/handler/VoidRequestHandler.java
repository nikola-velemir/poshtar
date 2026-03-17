package org.nikola.velemir.poshtar.core.request.handler;

import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.core.request.VoidRequest;

public interface VoidRequestHandler<TRequest extends VoidRequest> extends RequestHandler<TRequest, Unit> {
}
