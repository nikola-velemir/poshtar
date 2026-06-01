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

package io.github.nikola_velemir.poshtar.spring.adapter.internal.registry;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.notification.registry.AbstractNotificationRegistry;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;

import java.util.Map;
/**
 * Class maps specific notification type to its designated handler class, including Spring proxies.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class SpringNotificationRegistry extends AbstractNotificationRegistry implements ApplicationListener<ContextRefreshedEvent> {

    private final ApplicationContext context;
    private volatile boolean initialized;

    /**
     * Instantiates the registry, with the provided Spring context.
     * @param context Spring context, used for Posthar component discovery.
     */
    public SpringNotificationRegistry(ApplicationContext context) {
        this.context = context;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void init(ApplicationContext context) {
        Map<String, NotificationHandler> allHandlers  = context.getBeansOfType(NotificationHandler.class);

        for (NotificationHandler<Notification> handler : allHandlers.values()) {


            Class<?> targetClass = AopUtils.getTargetClass(handler);
            Class<?> notificationType = ResolvableType.forClass(targetClass)
                    .as(NotificationHandler.class)
                    .getGeneric(0).resolve();

            if (notificationType != null && Notification.class.isAssignableFrom(notificationType)) {
                @SuppressWarnings("unchecked")
                Class<? extends Notification> castedType = (Class<? extends Notification>) notificationType;
                register(castedType, (NotificationHandler) handler);
            }

        }
    }

    /**
     * Method is called upon context refresh to initialize the registry mappings.
     *
     * @param event Spring context refresh event.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == context && !initialized) {
            initialized = true;
            init(context);
        }
    }
}
