package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.injection;

import com.google.inject.Singleton;

@Singleton
public class DummyIncrementService {
    public int inc(int value){
        return value + 1;
    }
}
