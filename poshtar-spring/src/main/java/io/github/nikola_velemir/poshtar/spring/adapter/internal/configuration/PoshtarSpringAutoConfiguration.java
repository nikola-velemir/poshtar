package io.github.nikola_velemir.poshtar.spring.adapter.internal.configuration;


import io.github.nikola_velemir.poshtar.adapter.configuration.PipelineConfigurer;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.mediator.PoshtarImpl;
import io.github.nikola_velemir.poshtar.core.notification.registry.NotificationRegistry;
import io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry;
import io.github.nikola_velemir.poshtar.spring.adapter.internal.registry.SpringRequestRegistry;
import io.github.nikola_velemir.poshtar.spring.adapter.internal.registry.SpringNotificationRegistry;

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
    public PipelineConfigurer provideDefaultPipelineConfigurer() {
        return new PipelineConfigurer();
    }
    @Bean
    @ConditionalOnMissingBean
    public RequestRegistry provideRequestRegistry(PipelineConfigurer pipelineConfigurer){
        return new SpringRequestRegistry(context, pipelineConfigurer);
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
