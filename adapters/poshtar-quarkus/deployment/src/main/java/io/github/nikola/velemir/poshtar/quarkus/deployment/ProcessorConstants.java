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

package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

/**
 * Constants that {@link QuarkusPoshtarProcessor} uses during build time resolution.
 *
 * <p>
 * Contains static fields that are provided to {@link QuarkusPoshtarProcessor}.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class ProcessorConstants {
    public static final String FEATURE = "poshtar-quarkus";

    public static final String REQUEST_HANDLER_CLASS_NAME = RequestHandler.class.getName();
    public static final String REQUEST_CLASS_NAME = Request.class.getName();
    public static final String NOTIFICATION_HANDLER_CLASS_NAME = NotificationHandler.class.getName();
    public static final String NOTIFICATION_CLASS_NAME = Notification.class.getName();
    public static final String PIPELINE_CONFIGURATION_CLASS_NAME = PipelineConfiguration.class.getName();
    public static final String PIPELINE_BEHAVIOUR_CLASS_NAME = PipelineBehaviour.class.getName();
}
