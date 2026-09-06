/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
