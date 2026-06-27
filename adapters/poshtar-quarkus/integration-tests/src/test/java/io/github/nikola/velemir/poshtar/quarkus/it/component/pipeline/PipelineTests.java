package io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline;

import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.global.GlobalPipelineTestRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.order.OrderRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.specific.NotSpecificRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.specific.SpecificRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.validate.ValidationBehaviour;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.validate.ValidationRequest;
import io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.ping.PingRequestHandler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.quarkus.arc.Arc;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class PipelineTests {

    @Inject
    Poshtar poshtar;


    @Test
    void should_Call_Global_Pipeline() {
        assertDoesNotThrow(() -> {
            poshtar.send(new GlobalPipelineTestRequest());

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
    void should_Call_Specific_Pipeline() {
        var specificRequest = new SpecificRequest();
        poshtar.send(specificRequest);
        assertEquals(1, specificRequest.payload);

        var notSpecificRequest = new NotSpecificRequest();
        poshtar.send(notSpecificRequest);
        assertEquals(0, notSpecificRequest.payload);
    }

    @Test
    void should_Work_For_Validation() {
//        assertTrue(
//                Arc.container().instance(ValidationBehaviour.class).isAvailable(),
//                "Handler bean not registered through @Handler!"
//        );
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
