package io.github.nikola_velemir.poshtar.spring.adapter;

import io.github.nikola_velemir.poshtar.spring.adapter.internal.configuration.PoshtarSpringAutoConfiguration;
import io.github.nikola_velemir.poshtar.spring.adapter.internal.registrar.PoshtarSpringRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Configuration annotation that enables Posthar use in a spring project.
 * <p>
 * This annotation allows discovery of Poshtar components and registers them.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PoshtarSpringAutoConfiguration.class, PoshtarSpringRegistrar.class})
public @interface EnablePoshtar {
    /**
     * Specifies the packages to be scanned in search of Poshtar components.
     * <p>If not defined, scanning will begin from the base project package.</p>
     *
     * @return returns package names for scanning.
     */
    String[] basePackages() default {};
}
