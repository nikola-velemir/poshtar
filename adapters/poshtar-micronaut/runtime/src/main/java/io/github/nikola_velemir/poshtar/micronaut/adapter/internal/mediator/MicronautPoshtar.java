package io.github.nikola_velemir.poshtar.micronaut.adapter.internal.mediator;

import io.github.nikola_velemir.poshtar.core.mediator.PoshtarBase;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;

public class MicronautPoshtar extends PoshtarBase {
    /**
     * Instantiates a new {@link PoshtarBase} object, with provided {@link RequestRegistry} and {@link NotificationRegistry}.
     *
     * @param requestRegistry      provided request registry, holding all request to behavior-handler mappings.
     * @param notificationRegistry provided request registry, holding all notification to handler set mappings.
     */
    public MicronautPoshtar(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry) {
        super(requestRegistry, notificationRegistry);
    }
}
