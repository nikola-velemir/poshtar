package adapter;

import adapter.configuration.PoshtarSpringAutoConfiguration;
import adapter.registrar.PoshtarSpringRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PoshtarSpringAutoConfiguration.class})
public @interface EnablePoshtar {
}
