package org.nikola.velemir.poshtar.spring.adapter.request.deps.infrastructure.singleResponsibility;

import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.spring.adapter.request.deps.injection.InjectionRequest;

public interface HandlerMaskingInterface extends RequestHandler<InjectionRequest, String> {
}
