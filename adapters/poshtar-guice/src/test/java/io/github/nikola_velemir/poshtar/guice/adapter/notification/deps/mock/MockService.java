package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.mock;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;


@Singleton
public class MockService {
    MockServiceDeep service;
    @Inject

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
