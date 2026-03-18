package poshtar.tests;

import nikola.velemir.poshtar.spring.adapter.configuration.PipelineConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poshtar.tests.pipeline.deps.dead.DeadPipeline;
import poshtar.tests.pipeline.deps.global.GlobalTestPipeline;
import poshtar.tests.pipeline.deps.order.OrderFirstPipeline;
import poshtar.tests.pipeline.deps.order.OrderSecondPipeline;
import poshtar.tests.pipeline.deps.specific.SpecificPipeline;
import poshtar.tests.pipeline.deps.transactional.basic.TransactionalPipeline;
import poshtar.tests.pipeline.deps.transactional.mandatory.fail.FailMandatoryPipeline;
import poshtar.tests.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryPipeline;
import poshtar.tests.pipeline.deps.validate.ValidationBehaviour;

@Configuration
public class PipelineConfiguration {
    @Bean
    public PipelineConfigurer poshtarPipeline() {
        return new PipelineConfigurer()
                .add(GlobalTestPipeline.class)
                .add(ValidationBehaviour.class)
                .add(OrderFirstPipeline.class)
                .add(OrderSecondPipeline.class)
                .add(SpecificPipeline.class)
                .add(DeadPipeline.class)
                .add(TransactionalPipeline.class)
                .add(SucceedForMandatoryPipeline.class)
                .add(FailMandatoryPipeline.class);

    }
}
