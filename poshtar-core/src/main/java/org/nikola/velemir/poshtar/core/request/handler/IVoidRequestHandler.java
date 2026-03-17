package org.nikola.velemir.poshtar.core.request.handler;

import org.nikola.velemir.poshtar.core.types.Unit;
import org.nikola.velemir.poshtar.core.request.IVoidRequest;

public interface IVoidRequestHandler<TRequest extends IVoidRequest> extends IRequestHandler<TRequest, Unit> {
}
