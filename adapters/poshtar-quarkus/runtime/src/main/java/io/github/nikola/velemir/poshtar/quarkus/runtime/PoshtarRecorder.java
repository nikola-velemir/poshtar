package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;

import java.util.List;

@Recorder
public class PoshtarRecorder {

    public void registerHandlers(List<String> handlerClassNames) {
        QuarkusRequestRegistry registry = Arc.container()
                .instance(QuarkusRequestRegistry.class)
                .get();
        registry.initFromClassNames(handlerClassNames);
    }
}