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

package io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.basic.success;

import io.github.nikola.velemir.poshtar.quarkus.it.component.TestRepository;
import io.github.nikola.velemir.poshtar.quarkus.it.component.model.TestEntity;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;

@Behaviour
public class TransactionalPipeline implements PipelineBehaviour<TransactionalRequest, Unit> {

    @Inject
    TestRepository repository;

    public TransactionalPipeline(TestRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Unit handle(TransactionalRequest request, RequestDelegate<TransactionalRequest, Unit> requestDelegate) {
        request.payload += 1;

        boolean isActive = QuarkusTransaction.getStatus() == Status.STATUS_ACTIVE;
        System.out.println("Is Transaction REALLY Active? " + isActive);
        var res = requestDelegate.handle(request);
        System.out.println("Called transactional behaviour");

        var te = new TestEntity("From transactional behaviour");
        repository.persistAndFlush(te); // 3. Fixed: Panache uses persistAndFlush

        return res;
    }
}
