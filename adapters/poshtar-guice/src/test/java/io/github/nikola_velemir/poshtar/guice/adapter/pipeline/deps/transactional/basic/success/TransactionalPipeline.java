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
