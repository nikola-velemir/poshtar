package io.github.nikola.velemir.poshtar.quarkus.it.component;

import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class PoshtarExtensionTest {

    @Inject
    Poshtar poshtar;

    @Test
    void poshtarBeanIsAvailable() {
        assertNotNull(poshtar);
    }
}