package nikola.velemir.poshtar.spring.adapter.configuration;


import nikola.velemir.poshtar.spring.adapter.injection.registry.SpringRequestRegistry;
import nikola.velemir.poshtar.spring.adapter.injection.registry.SpringNotificationRegistry;

import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.core.mediator.PoshtarImpl;
import org.nikola.velemir.poshtar.core.notification.registry.NotificationRegistry;
import org.nikola.velemir.poshtar.core.request.registry.RequestRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PoshtarSpringAutoConfiguration {
    private final ApplicationContext context;

    public PoshtarSpringAutoConfiguration(ApplicationContext context) {
        this.context = context;
    }
    @Bean
    @ConditionalOnMissingBean
    public PipelineConfigurer defaultPipelineConfigurer() {
        return new PipelineConfigurer();
    }
    @Bean
    @ConditionalOnMissingBean
    public RequestRegistry provideRequestRegistry(){
        return new SpringRequestRegistry(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public NotificationRegistry provideNotificationRegistry(){
        return new SpringNotificationRegistry(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public Poshtar configurePoshtar(RequestRegistry handlerRegistry, NotificationRegistry notificationRegistry){
        return new PoshtarImpl(handlerRegistry, notificationRegistry);
    }
}
