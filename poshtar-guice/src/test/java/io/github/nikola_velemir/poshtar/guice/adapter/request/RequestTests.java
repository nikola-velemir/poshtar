package io.github.nikola_velemir.poshtar.guice.adapter.request;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.infrastructure.NotFoundRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection.InjectionResponse;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.nullRequest.NullRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping.PingRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail.FailForTransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.TransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success.UpdateTransactionalRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTests {
    private static Poshtar poshtar;
    private static Injector injector;

    @BeforeAll
    static void setUp() {
        injector = Guice.createInjector(new TestModule());

        poshtar = injector.getInstance(Poshtar.class);
    }

    @Test
    void should_Register_And_Execute_Handler_Automatically() {

        String response = poshtar.send(new PingRequest("Hello Poshtar"));

        assert response.equals("Pong: Hello Poshtar") : "Wrong response!";
        System.out.println(">>> TEST PASSED: " + response);

    }

    @Test
    void handles_Null_Send() {
        NullRequest request = null;
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () -> poshtar.send(request));
        assertInstanceOf(IllegalArgumentException.class, ex);
        String expected = "Request cannot be null";
        String actual = ex.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    void should_fail_for_unregistered_handler() {
        NotFoundRequest request = new NotFoundRequest();
        Exception ex = assertThrowsExactly(HandlerNotFoundException.class, () -> poshtar.send(request));
        assertInstanceOf(HandlerNotFoundException.class, ex);
        String expectedMessage = "[PoshtaR] No handler found for type: [NotFoundRequest].";
        String actualMessage = ex.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void should_Register_And_Inject_Service() {
        InjectionResponse response = poshtar.send(new InjectionRequest("Hello Poshtar"));

        assert response.payload().equals("Request with Logged: Hello Poshtar") : "Incorrect response!";
        System.out.println(">>> TEST PROŠAO: " + response);
    }

    @Test
    void should_Pass_With_At_Transactional() {

        assertDoesNotThrow(() -> {
            String response = poshtar.send(new TransactionalRequest("Hello Poshtar"));
            assert response.equals("Request with Hello Poshtar") : "Response is incorrect";
            System.out.println(">>> TEST PASSED: " + response);

            EntityManager em = injector.getInstance(EntityManager.class);

            em.getTransaction().begin();
            List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Hello Poshtar'", TestEntity.class).getResultList();
            em.getTransaction().commit();
            assertFalse(results.isEmpty(), "Transaction did not commit!.");

            var entity = results.getFirst();

            poshtar.send(new UpdateTransactionalRequest(entity.getId(), "Updated"));

            em.getTransaction().begin();
            List<TestEntity> updateResults = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Updated'", TestEntity.class).getResultList();
            em.getTransaction().commit();
            assertFalse(updateResults.isEmpty(), "Transaction did not commit!.");

        });
    }

    @Test
    void should_Fail_With_At_Transactional() {

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> {
            String response = poshtar.send(new FailForTransactionalRequest("Fail for poshtar"));
            assert !response.equals("Fail for poshtar") : "Response is incorrect";
            System.out.println(">>> TEST PASSED: " + response);
        });
        String actual = ex.getMessage();
        String expected = "Simulated entity persistance failed!";
        assertEquals(expected, actual);

        EntityManager em = injector.getInstance(EntityManager.class);

        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Fail for poshtar'", TestEntity.class).getResultList();
        em.getTransaction().commit();
        assertTrue(results.isEmpty(), "Transaction did not roll back! Entity was saved.");
    }
}
