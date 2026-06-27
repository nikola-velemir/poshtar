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

package io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.basic.fail;

import io.github.nikola.velemir.poshtar.quarkus.it.component.TestRepository;
import io.github.nikola.velemir.poshtar.quarkus.it.component.model.TestEntity;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Behaviour
public class FailTransactionalPipeline implements PipelineBehaviour<FailTransactionalRequest, Unit> {

    @Inject
    TestRepository testRepository;

    public FailTransactionalPipeline(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    @Transactional
    public Unit handle(FailTransactionalRequest request, RequestDelegate<FailTransactionalRequest, Unit> delegate) {
        var te = new TestEntity(request.payload());
        testRepository.persist(te);
        throw new RuntimeException("Failing on purpose");
    }
}
