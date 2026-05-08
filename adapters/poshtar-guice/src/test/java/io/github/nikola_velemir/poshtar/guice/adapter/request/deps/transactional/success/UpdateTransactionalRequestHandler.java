/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
