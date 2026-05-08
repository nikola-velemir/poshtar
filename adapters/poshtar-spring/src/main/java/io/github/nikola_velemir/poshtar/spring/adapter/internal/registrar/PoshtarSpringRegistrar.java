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

package io.github.nikola_velemir.poshtar.spring.adapter.internal.registrar;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.spring.adapter.EnablePoshtar;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.Objects;
/**
 * Class used for component scanning and their instantiation as spring beans. Searches through packages provided by {@link EnablePoshtar}.
 * Classes annotated with {@link Handler} and {@link Behaviour} are introduced into Spring context as singleton spring beans.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class PoshtarSpringRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        buildFilters(scanner);

        Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnablePoshtar.class.getName());
        String[] packages = discoverPackages(importingClassMetadata, attrs);

        for (String basePackage : packages) {
            for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
                registerBean(registry, bd);
            }
        }

    }

    private static void registerBean(BeanDefinitionRegistry registry, BeanDefinition bd) {
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClassName(bd.getBeanClassName());
        beanDef.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(Objects.requireNonNull(bd.getBeanClassName()), beanDef);
    }

    private static String[] discoverPackages(@NonNull AnnotationMetadata importingClassMetadata, Map<String, Object> attrs) {
        return (attrs != null && ((String[]) attrs.get("basePackages")).length > 0)
                ? (String[]) attrs.get("basePackages")
                : new String[]{ ClassUtils.getPackageName(importingClassMetadata.getClassName()) };
    }

    private static void buildFilters(ClassPathScanningCandidateComponentProvider scanner) {
        scanner.addIncludeFilter(new AnnotationTypeFilter(Handler.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Behaviour.class));
    }

}
