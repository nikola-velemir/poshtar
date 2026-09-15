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

package io.github.nikola_velemir.poshtar.micronaut.it.notification.deps.forget;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.micronaut.scheduling.annotation.Async;
import io.micronaut.transaction.TransactionDefinition;
import io.micronaut.transaction.annotation.Transactional;

@Handler
public class FailForForgetThirdHandler implements NotificationHandler<FailForForgetNotification> {
    @Override
    @Async
    @Transactional(propagation = TransactionDefinition.Propagation.MANDATORY)
    public void handle(FailForForgetNotification failForForgetNotification) {
        throw new RuntimeException("Purposefully causing exception");
    }
}
