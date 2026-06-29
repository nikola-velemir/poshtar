/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.spring.adapter;

import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.dead.DeadPipelineCatcher;
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
                .add(DeadPipelineCatcher.class)
                .add(TransactionalPipeline.class)
                .add(SucceedForMandatoryPipeline.class)
                .add(FailMandatoryPipeline.class)
                .add(FailTransactionalPipeline.class);

    }
}
