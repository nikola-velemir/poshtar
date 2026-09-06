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
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Handler
public class TransactionalRequestHandler implements RequestHandler<TransactionalRequest, Unit> {
    private final Provider<EntityManager> entityManagerProvider;
    @Inject
    public TransactionalRequestHandler(Provider<EntityManager> provider) {
        entityManagerProvider = provider;
    }
    @Override
    @Transactional
    public Unit handle(TransactionalRequest request) {

        EntityManager em = entityManagerProvider.get();

        // Checking the status
        boolean isActive = em.getTransaction().isActive();

        System.out.println(">>> Is Transaction Active? " + isActive);
        assertTrue(isActive);

        request.payload += 1;
        return Unit.Value;
    }
}
