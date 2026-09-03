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

package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.mock;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import jakarta.inject.Inject;

@Handler
public class MockChainedFirstRequestHandler implements RequestHandler<MockChainedFirstRequest, MockChainedResponse> {
    @Inject
    Poshtar poshtar;

    public MockChainedFirstRequestHandler(Poshtar poshtar) {
        this.poshtar = poshtar;
    }

    @Override
    public MockChainedResponse handle(MockChainedFirstRequest mockChainedFirstRequest) {
        var response = poshtar.send(new MockChainedSecondRequest("Hello"));
        return new MockChainedResponse(response);
    }
}
