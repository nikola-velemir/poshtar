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

package io.github.nikola_velemir.poshtar.guice.adapter.notification.deps.injection;

import jakarta.inject.Inject;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;


@Handler
public class InjectionNotificationThirdHandler implements NotificationHandler<InjectionNotification> {
    private final DummyIncrementService incrementService;

    @Inject
    public InjectionNotificationThirdHandler(DummyIncrementService incrementService) {
        this.incrementService = incrementService;
    }

    @Override
    public void handle(InjectionNotification injectionNotification) {
        try {
            Thread.sleep(1000);
            System.out.println("Ran third Injection handler");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Handler thread was interrupted during sleep");
        }

        injectionNotification.value = incrementService.inc(injectionNotification.value);
    }
}
