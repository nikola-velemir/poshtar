package org.nikola.velemir.poshtar.guice.adapter.request.deps.injection;


import org.nikola.velemir.poshtar.core.request.Request;

public final class InjectionRequest implements Request<InjectionResponse> {
    public String payload;

    public InjectionRequest(String _payload) {
        payload = _payload;
    }
}
