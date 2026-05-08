/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * <p><b>Example Usage:</b></p>
 * <pre>
 * &#64;EnablePoshtar
 * public class MyApplication {
 * }
 * </pre>
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
