package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception.BeanNotFoundException;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
/**
 * Quarkus-specific implementation of the notification registry that integrates
 * notification dispatching with the active CDI container.
 *
 * <p>
 * This class handles the runtime assembly of notification routing structures by converting
 * discovered class types into proxy-managed, container-backed bean instances.
 * </p>
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
@ApplicationScoped
public class QuarkusNotificationRegistry extends AbstractNotificationRegistry {
    /**
     * Resolves the CDI bean for a given notification handler class
     * and maps it to its corresponding notification.
     *
     * @param handlerClass the class of the target notification handler
     * @param notifClass the class of the event notification being handled
     * @param bm the active CDI {@link BeanManager} used to query the container and instantiate references
     * @throws BeanNotFoundException if the target handler class cannot be located as a registered CDI bean
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerFromClass(Class<?> handlerClass, Class<?> notifClass, BeanManager bm) {
        Bean<?> bean = bm.getBeans(handlerClass).stream().findFirst()
                .orElseThrow(() -> new BeanNotFoundException(handlerClass));
        NotificationHandler handler = (NotificationHandler) bm.getReference(
                bean, handlerClass, bm.createCreationalContext(bean));

        register((Class) notifClass, handler);

    }
}