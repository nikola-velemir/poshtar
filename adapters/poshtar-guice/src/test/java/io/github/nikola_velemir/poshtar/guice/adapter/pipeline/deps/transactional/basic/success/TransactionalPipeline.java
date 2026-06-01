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

package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import jakarta.persistence.EntityManager;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Behaviour
public class TransactionalPipeline implements PipelineBehaviour<TransactionalRequest, Unit> {
    private final Provider<EntityManager> emProvider;

    @Inject
    public TransactionalPipeline(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }

    @Override
    @Transactional
    public Unit handle(TransactionalRequest request, RequestDelegate<TransactionalRequest, Unit> requestDelegate) {
        request.payload += 1;
        var res = requestDelegate.handle(request);
        System.out.println("Called transactional behaviour");
        EntityManager em = emProvider.get();

        // Checking the status
        boolean isActive = em.getTransaction().isActive();

        System.out.println(">>> Is Transaction Active? " + isActive);
        assertTrue(isActive);

        TestEntity entity = new TestEntity("From transactional pipeline");
        em.persist(entity);
        return res;
    }
}
