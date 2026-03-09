package poshtar.tests.infrastructure;

import org.example.core.exceptions.HandlerNotFoundException;
import org.example.core.mediator.IMediator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import poshtar.tests.TestApplication;
import poshtar.tests.infrastructure.deps.NotFoundRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestApplication.class})
public class InfrastructureTests {
    @Autowired
    private IMediator mediator;

    @Test
    void should_fail_for_unregistered_handler() {
       NotFoundRequest request = new NotFoundRequest();
       Exception exception = assertThrowsExactly(HandlerNotFoundException.class,()->{
           mediator.send(request);
       });
       String expectedMessage = "[PoshtaR] No handler found for type: [NotFoundRequest].";
       String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }


}
