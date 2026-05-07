package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.infrastructure.singleResponsibility;

import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.spring.adapter.request.deps.injection.InjectionRequest;

public interface HandlerMaskingInterface extends RequestHandler<InjectionRequest, String> {
}
