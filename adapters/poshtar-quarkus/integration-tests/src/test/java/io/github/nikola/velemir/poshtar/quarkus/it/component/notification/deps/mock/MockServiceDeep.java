package io.github.nikola.velemir.poshtar.quarkus.it.component.notification.deps.mock;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MockServiceDeep {
    public String getHi() {
        return "Hi";
    }
}
