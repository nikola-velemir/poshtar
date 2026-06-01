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

package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.guice.adapter.model.TestEntity;

@Handler
public class UpdateTransactionalRequestHandler implements RequestHandler<UpdateTransactionalRequest, Unit> {
    private final Provider<EntityManager> emProvider;
    @Inject
    public UpdateTransactionalRequestHandler(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }


    @Override
    @Transactional
    public Unit handle(UpdateTransactionalRequest request) {
        EntityManager em = emProvider.get();

        TestEntity entity = em.find(TestEntity.class, request.id());

        if (entity == null) {
            throw new EntityNotFoundException("Entity with ID " + request.id() + " not found");
        }

        entity.setData(request.data());

        return Unit.Value;
    }
}
