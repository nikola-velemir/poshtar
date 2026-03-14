package adapter.configuration;


import adapter.registry.SpringRequestRegistry;
import adapter.registry.SpringNotificationRegistry;
import org.example.core.mediator.IMediator;
import org.example.core.notification.registry.INotificationRegistry;
import org.example.core.request.registry.IRequestRegistry;
import org.example.impl.Mediator;
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
    public IRequestRegistry handlerRegistry(){
        return new SpringRequestRegistry(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public INotificationRegistry notificationRegistry(){
        return new SpringNotificationRegistry(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public IMediator mediator(IRequestRegistry handlerRegistry,INotificationRegistry notificationRegistry){
        return new Mediator(handlerRegistry, notificationRegistry);
    }
}
