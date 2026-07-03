package io.github.nikola_velemir.poshtar.spring.adapter.internal.mediator;

import io.github.nikola_velemir.poshtar.core.mediator.PoshtarBase;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;

/**
 * Class represents a concrete implementation of {@link PoshtarBase} to satisfy bridge pattern.
 * It is a provided implementation to client classes which use {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar} interface.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public final class SpringPoshtar extends PoshtarBase {
    public SpringPoshtar(RequestRegistry requestRegistry, NotificationRegistry notificationRegistry) {
        super(requestRegistry, notificationRegistry);
    }
}
