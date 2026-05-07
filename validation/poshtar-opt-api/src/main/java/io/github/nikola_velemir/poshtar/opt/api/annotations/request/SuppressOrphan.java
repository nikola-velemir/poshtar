package io.github.nikola_velemir.poshtar.opt.api.annotations.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to suppress the validation processor rule for orphaned requests.
 *
 * <p>
 * Annotation processor by default disallows existence of requests that do not have a designated handler.
 * Using this annotation you are able to override this rule, at your own will.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressOrphan {
}
