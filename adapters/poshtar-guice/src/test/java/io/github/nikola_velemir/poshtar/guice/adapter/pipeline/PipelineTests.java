/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.guice.adapter.pipeline;

import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
    private Poshtar poshtar;
    private Injector injector;

    @BeforeEach
    void setUp() {
        injector = Guice.createInjector(new TestModule());
        poshtar = injector.getInstance(Poshtar.class);

        // Clean DB state before each test
        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();
        em.createQuery("DELETE FROM TestEntity").executeUpdate();
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        EntityManager em = injector.getInstance(EntityManager.class);
        if (em.isOpen()) em.close();
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
    void should_Call_Dead_Pipeline() {
        var deadRequest = new DeadRequest();
        assertDoesNotThrow(() -> {
            var result = poshtar.send(deadRequest);
            assertNull(result);
        });
    }

    @Test
    void should_Respect_Order() {
        var orderRequest = new OrderRequest();
        assertDoesNotThrow(() -> poshtar.send(orderRequest));
        assertEquals(3, orderRequest.payload);
    }

    @Test
    void should_Work_For_Validation() {
        var goodValidationRequest = new ValidationRequest(1);
        assertDoesNotThrow(() -> {
            var response = poshtar.send(goodValidationRequest);
            assertEquals(2, response);
        });

        var badValidationRequest = new ValidationRequest(0);
        Exception ex = assertThrowsExactly(IllegalArgumentException.class, () ->
                poshtar.send(badValidationRequest));
        assertEquals(0, badValidationRequest.payload());
        assertEquals("Payload is wrong", ex.getMessage());
    }

    @Test
    void should_Pass_For_Transactional() {
        var transactionalRequest = new TransactionalRequest();
        assertDoesNotThrow(() -> poshtar.send(transactionalRequest));
        assertEquals(2, transactionalRequest.payload);

        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery(
                "SELECT d FROM TestEntity d WHERE d.data = 'From transactional pipeline'",
                TestEntity.class).getResultList();
        em.getTransaction().commit();

        assertFalse(results.isEmpty(), "Transaction did not commit");
        assertEquals(1, results.size());
    }

    @Test
    void should_Fail_For_Transactional() {
        var failTransactionalRequest = new FailTransactionalRequest("Fail from transactional pipeline");
        assertThrowsExactly(RuntimeException.class, () ->
                poshtar.send(failTransactionalRequest));

        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();
        List<TestEntity> results = em.createQuery(
                "SELECT d FROM TestEntity d WHERE d.data = 'Fail from transactional pipeline'",
                TestEntity.class).getResultList();
        em.getTransaction().commit();

        assertTrue(results.isEmpty(), "Transaction should have rolled back");
    }
}