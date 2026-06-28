package io.github.nikola_velemir.poshtar.spring.adapter.internal.mediator;

import io.github.nikola_velemir.poshtar.core.mediator.PoshtarBase;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;

public final class SpringPoshtar extends PoshtarBase {
    public SpringPoshtar(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry) {
        super(requestRegistry, notificationRegistry);
    }
}
