package io.github.nikola_velemir.poshtar.spring.adapter.notification.deps.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockService {
    @Autowired
    private final MockServiceDeep service;

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
