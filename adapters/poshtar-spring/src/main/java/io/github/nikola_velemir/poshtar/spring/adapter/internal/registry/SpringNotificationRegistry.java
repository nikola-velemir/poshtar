/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
