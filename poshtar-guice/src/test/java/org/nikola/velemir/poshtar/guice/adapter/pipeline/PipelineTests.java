package org.nikola.velemir.poshtar.guice.adapter.pipeline;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.guice.adapter.TestModule;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadRequest;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.order.OrderRequest;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.specific.NotSpecificRequest;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.specific.SpecificRequest;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationRequest;


import static org.junit.jupiter.api.Assertions.*;

public class PipelineTests {
    private static Poshtar poshtar;

    @BeforeAll
    static void setUp() {
        Injector injector = Guice.createInjector(new TestModule());

        poshtar = injector.getInstance(Poshtar.class);
    }

    @Test
    void should_Call_Specific_Pipeline() {
        var specificRequest = new SpecificRequest();
        poshtar.send(specificRequest);
        assertEquals(1, specificRequest.payload);

        var notSpecificRequest = new NotSpecificRequest();
        poshtar.send(notSpecificRequest);
        assertEquals(0, notSpecificRequest.payload);
    }

    @Test
    void should_call_Dead_Pipeline() {
        var deadRequest = new DeadRequest();
        assertDoesNotThrow(() -> {
            var result = poshtar.send(deadRequest);
            assertNull(result);
        });
    }

    @Test
    void should_Respect_Order() {
        var orderRequest = new OrderRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(orderRequest);

        });
        assertEquals(3, orderRequest.payload);
    }


    @Test
    void should_Work_For_Validation() {

        var goodValidationRequest = new ValidationRequest(1);
        assertDoesNotThrow(() -> {
            var response = poshtar.send(goodValidationRequest);
            assertEquals(2, response);
        });
        var badValidatioNRequest = new ValidationRequest(0);
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> {
            poshtar.send(badValidatioNRequest);
        });
        assertEquals(0, badValidatioNRequest.payload());
        String actual = ex.getMessage();
        String expected = "Payload is wrong";
        assertEquals(expected, actual);
    }
}
