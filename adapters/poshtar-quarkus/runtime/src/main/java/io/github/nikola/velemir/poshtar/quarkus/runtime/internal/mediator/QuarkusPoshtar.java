package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.mediator;

import io.github.nikola_velemir.poshtar.core.mediator.PoshtarBase;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;

public final class QuarkusPoshtar extends PoshtarBase{
    public QuarkusPoshtar(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry) {
        super(requestRegistry, notificationRegistry);
    }
}
