package io.github.nikola_velemir.poshtar.spring.adapter;

import io.github.nikola_velemir.poshtar.spring.adapter.internal.configuration.PoshtarSpringAutoConfiguration;
import io.github.nikola_velemir.poshtar.spring.adapter.internal.registrar.PoshtarSpringRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PoshtarSpringAutoConfiguration.class, PoshtarSpringRegistrar.class})
public @interface EnablePoshtar {
    String[] basePackages() default {};
}
