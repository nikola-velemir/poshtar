package io.github.nikola_velemir.poshtar.validator.api.annotations.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation used to override validation processor's rule that prevents direct
 * injection and instantiation of PoshtaR components for test purposes.
 *
 * <p>
 * Annotation processor by default disallows instantiation and injection (through fields, methods and constructros).
 * Unit tests would need to test the PoshtaR component, therefore injection of mocks would become cumbersome.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface OverruleNoInjection {
}
