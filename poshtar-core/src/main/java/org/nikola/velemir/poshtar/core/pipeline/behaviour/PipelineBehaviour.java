package org.nikola.velemir.poshtar.core.pipeline.behaviour;

import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.request.Request;

public interface PipelineBehaviour<TRequest extends Request<TResponse>, TResponse> {

    TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> next);
}
