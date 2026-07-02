package io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.mock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MockService {
    @Inject
    MockServiceDeep service;

    public MockService(MockServiceDeep service) {
        this.service = service;
    }

    public String getHello() {
        return "Hello";
    }

    public String getHi() {
        return service.getHi();
    }
}
