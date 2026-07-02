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

package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.transactional.basic;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.transaction.Status;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Handler
public class TransactionalRequestHandler implements RequestHandler<TransactionalRequest, String> {

    public TransactionalRequestHandler() {
    }

    @Transactional
    @Override
    public String handle(TransactionalRequest injectionRequest) {

        boolean isActive = QuarkusTransaction.getStatus() == Status.STATUS_ACTIVE;
        System.out.println("Is Transaction REALLY Active? " + isActive);
        assertTrue(isActive);
        return "Request with " + injectionRequest.payload();
    }
}
