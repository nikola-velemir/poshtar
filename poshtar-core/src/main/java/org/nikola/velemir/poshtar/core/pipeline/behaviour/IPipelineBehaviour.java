package org.nikola.velemir.poshtar.core.pipeline.behaviour;

import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.request.IRequest;

public interface IPipelineBehaviour<TRequest extends IRequest<TResponse>, TResponse> {

    TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> next);
}
