package io.github.nikola_velemir.poshtar.opt.api.annotations.pipeline;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to suppress the validation processor rule for dead (short-circuited) pipelines.
 *
 * <p>
 * Annotation processor by default disallows dead pipelines, by scanning the method AST.
 * Using this annotation you are able to override this rule, if your logic is correct but the processor does not allow you to compile the project.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressDead {
}
