package io.github.nikola_velemir.poshtar.spring.adapter;

import io.github.nikola_velemir.poshtar.adapter.configuration.PipelineConfigurer;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalPipeline;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.dead.DeadPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.global.GlobalTestPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.order.OrderFirstPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.order.OrderSecondPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.specific.SpecificPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.basic.success.TransactionalPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.fail.FailMandatoryPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryPipeline;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.validate.ValidationBehaviour;

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
                .add(FailMandatoryPipeline.class)
                .add(FailTransactionalPipeline.class);

    }
}
