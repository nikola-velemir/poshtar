package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.github.nikola_velemir.poshtar.core.notification.Notification;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
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

    public static final String PIPELINE_BEHAVIOUR_CLASS_NAME = PipelineBehaviour.class.getName();
}
