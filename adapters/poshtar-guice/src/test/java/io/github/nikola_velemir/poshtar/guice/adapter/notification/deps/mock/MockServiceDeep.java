package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.mock;

import com.google.inject.Singleton;

@Singleton
public class MockServiceDeep {
    public String getHi() {
        return "Hi";
    }
}
