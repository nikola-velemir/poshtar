package org.nikola.velemir.poshtar.spring.adapter;

import nikola.velemir.poshtar.spring.adapter.configuration.PipelineConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.dead.DeadPipeline;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.global.GlobalTestPipeline;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.order.OrderFirstPipeline;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.order.OrderSecondPipeline;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.specific.SpecificPipeline;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.TransactionalPipeline;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.fail.FailMandatoryPipeline;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryPipeline;
import org.nikola.velemir.poshtar.spring.adapter.pipeline.deps.validate.ValidationBehaviour;

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
