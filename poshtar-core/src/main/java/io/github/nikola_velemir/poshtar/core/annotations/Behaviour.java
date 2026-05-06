package io.github.nikola_velemir.poshtar.core.annotations;

import javax.inject.Named;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for {@link io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour} discovery.
 * <p>Developer is to annotate their class with this annotation,
 * so discovery logic may find it and integrate it into its dependency context</p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Named
public @interface Behaviour {
}
