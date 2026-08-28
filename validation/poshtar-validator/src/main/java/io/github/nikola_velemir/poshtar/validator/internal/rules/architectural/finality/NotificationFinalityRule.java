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

package io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.finality;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import java.util.Set;

class NotificationFinalityRule extends FinalityRule {
    @Override
    protected String getViolationMessage(String fqn) {
        String FINALITY_VIOLATED_MESSAGE = "PoshtaR: Finality Violated! Notification '%s' must be final or a record!";
        return String.format(FINALITY_VIOLATED_MESSAGE, fqn);
    }

    @Override
    protected Set<String> getFQNs(ProcessorContext ctx) {
        return ctx.getKnownNotifications();
    }
}