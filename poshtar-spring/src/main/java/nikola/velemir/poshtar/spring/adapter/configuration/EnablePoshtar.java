package nikola.velemir.poshtar.spring.adapter.configuration;

import nikola.velemir.poshtar.spring.adapter.discovery.registrar.PoshtarSpringRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PoshtarSpringAutoConfiguration.class, PoshtarSpringRegistrar.class})
public @interface EnablePoshtar {
    String[] basePackages() default {};
}
