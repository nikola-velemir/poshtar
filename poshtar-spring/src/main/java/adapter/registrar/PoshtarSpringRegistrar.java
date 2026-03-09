package adapter.registrar;

import adapter.EnablePoshtar;
import org.example.core.annotations.NotificationHandler;
import org.example.core.annotations.PipelineBehaviour;
import org.example.core.annotations.RequestHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.Objects;

public class PoshtarSpringRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        buildFilters(scanner);

        Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnablePoshtar.class.getName());
        String[] packages = (attrs != null && ((String[]) attrs.get("basePackages")).length > 0)
                ? (String[]) attrs.get("basePackages")
                : new String[]{ ClassUtils.getPackageName(importingClassMetadata.getClassName()) };

        for (String basePackage : packages) {
            for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
                GenericBeanDefinition beanDef = new GenericBeanDefinition();
                beanDef.setBeanClassName(bd.getBeanClassName());
                beanDef.setScope(BeanDefinition.SCOPE_SINGLETON);
                registry.registerBeanDefinition(Objects.requireNonNull(bd.getBeanClassName()), beanDef);
            }
        }

    }

    private static void buildFilters(ClassPathScanningCandidateComponentProvider scanner) {
        scanner.addIncludeFilter(new AnnotationTypeFilter(RequestHandler.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(NotificationHandler.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(PipelineBehaviour.class));
    }

    @Nullable
    private static String findBasePackage(BeanDefinitionRegistry registry) {
        String basePackage = null;
        for (String beanName : registry.getBeanDefinitionNames()) {
            BeanDefinition bd = registry.getBeanDefinition(beanName);
            if (bd.getBeanClassName() == null) continue;
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                if (clazz.isAnnotationPresent(EnablePoshtar.class)) {
                    basePackage = clazz.getPackageName();
                    break;
                }
            } catch (ClassNotFoundException ignored) {}
        }
        return basePackage;
    }
}
