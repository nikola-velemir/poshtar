package io.github.nikola_velemir.poshtar.guice.adapter.pipeline;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order.OrderRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.specific.NotSpecificRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.specific.SpecificRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success.TransactionalRequest;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationRequest;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PipelineTests {
    private static Poshtar poshtar;
    private static Injector injector;

    @BeforeAll
    static void setUp() {
        injector = Guice.createInjector(new TestModule());

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

    @Test
    void should_Pass_For_Transactional() {

        var transactionalRequest = new TransactionalRequest();
        assertDoesNotThrow(() -> {
            poshtar.send(transactionalRequest);

            EntityManager em = injector.getInstance(EntityManager.class);

            em.getTransaction().begin();
            List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'From transactional pipeline'", TestEntity.class).getResultList();
            em.getTransaction().commit();
            assertFalse(results.isEmpty(), "Transaction did not commit!.");
        });
        assertEquals(2, transactionalRequest.payload);
    }
    @Test
    void should_Fail_For_Transactional(){
        var failTransactionalRequest = new FailTransactionalRequest("Fail from transactional pipeline");
        assertThrowsExactly(RuntimeException.class, ()->{
           poshtar.send(failTransactionalRequest);

        });
        EntityManager em = injector.getInstance(EntityManager.class);

        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery("SELECT d FROM TestEntity d where d.data = 'Fail from transactional pipeline'", TestEntity.class).getResultList();
        em.getTransaction().commit();
        assertTrue(results.isEmpty(), "Transaction did commit!.");
    }
}
