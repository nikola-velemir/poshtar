package org.example.core.request.handler;

import org.example.core.types.Unit;
import org.example.core.request.IVoidRequest;

public interface IVoidRequestHandler<TRequest extends IVoidRequest> extends IRequestHandler<TRequest, Unit> {
}
