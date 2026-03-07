package adapter.registrar;

import org.example.core.annotations.NotificationHandler;
import org.example.core.annotations.RequestHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.List;

public class PoshtarSpringRegistrar implements ImportBeanDefinitionRegistrar {
    private BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RequestHandler.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(NotificationHandler.class));

        List<String> packages;
        try {
            packages = AutoConfigurationPackages.get((BeanFactory) registry);
        } catch (Exception e) {
            String className = importingClassMetadata.getClassName();
            packages = List.of(className.substring(0, className.lastIndexOf('.')));
        }

        for (String basePackage : packages) {
            for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
                GenericBeanDefinition beanDef = new GenericBeanDefinition();
                beanDef.setBeanClassName(bd.getBeanClassName());
                beanDef.setScope(BeanDefinition.SCOPE_SINGLETON);

                // Koristimo puno ime klase kao ID beana da ne bi bilo konflikta
                registry.registerBeanDefinition(bd.getBeanClassName(), beanDef);
            }
        }

    }
}
