package adapter;


import adapter.registrar.PoshtarSpringRegistrar;
import adapter.registry.SpringHandlerRegistry;
import adapter.registry.SpringNotificationRegistry;
import org.example.core.mediator.IMediator;
import org.example.core.notification.registry.INotificationRegistry;
import org.example.core.pipeline.IPipelineRegistry;
import org.example.core.request.registry.IRequestRegistry;
import org.example.impl.Mediator;
import org.example.impl.pipeline.PipelineRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PoshtarSpringRegistrar.class)
public class PoshtarSpringAutoConfiguration {
    private final ApplicationContext context;

    public PoshtarSpringAutoConfiguration(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    @ConditionalOnMissingBean
    public IRequestRegistry handlerRegistry(){
        return new SpringHandlerRegistry(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public INotificationRegistry notificationRegistry(){
        return new SpringNotificationRegistry(context);
    }
    @Bean
    @ConditionalOnMissingBean
    public IPipelineRegistry pipelineRegistry() {
        // Skenira sve IPipelineBehaviour bean-ove u sistemu
        return new PipelineRegistry();
    }
    @Bean
    @ConditionalOnMissingBean
    public IMediator mediator(IRequestRegistry handlerRegistry,INotificationRegistry notificationRegistry, IPipelineRegistry pipelineRegistry){
        return new Mediator(handlerRegistry, notificationRegistry, pipelineRegistry);
    }
}
