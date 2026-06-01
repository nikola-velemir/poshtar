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

package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.success;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Behaviour
public class SucceedForMandatoryPipeline implements PipelineBehaviour<SucceedForMandatoryRequest, Unit> {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Unit handle(SucceedForMandatoryRequest request, RequestDelegate<SucceedForMandatoryRequest, Unit> requestDelegate) {
        System.out.println("Called inside mandatory transaction");
        boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("Is Transaction REALLY Active? " + isActive);
        return requestDelegate.handle(request);
    }
}
