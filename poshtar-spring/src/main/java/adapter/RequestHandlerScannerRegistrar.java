package adapter;

import org.example.core.annotations.RequestHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.List;

public class RequestHandlerScannerRegistrar implements ImportBeanDefinitionRegistrar {
    private BeanFactory beanFactory;

    public void  setBeanFactory(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RequestHandler.class));

        List<String> packages;
        try {
            packages = AutoConfigurationPackages.get((BeanFactory) registry);
        } catch (Exception e) {
            // Backup: Ako AutoConfigurationPackages ne prođe, uzmi paket u kom se nalazi klasa koja uvozi (TestApplication)
            String className = importingClassMetadata.getClassName();
            packages = List.of(className.substring(0, className.lastIndexOf('.')));
        }

        for (String basePackage : packages) {
            for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
                GenericBeanDefinition beanDef = new GenericBeanDefinition();
                beanDef.setBeanClassName(bd.getBeanClassName());
                beanDef.setScope(BeanDefinition.SCOPE_SINGLETON);

                // Registrujemo bean koristeći ime klase
                registry.registerBeanDefinition(bd.getBeanClassName(), beanDef);
            }
        }
    }
}
