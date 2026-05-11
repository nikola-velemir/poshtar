/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.spring.adapter;

import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
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
public class PipelineConfig {
    @Bean
    public PipelineConfiguration poshtarPipeline() {
        return new PipelineConfiguration()
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
