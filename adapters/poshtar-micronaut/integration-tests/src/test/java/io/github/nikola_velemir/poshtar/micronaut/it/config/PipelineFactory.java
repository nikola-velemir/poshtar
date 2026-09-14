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

package io.github.nikola_velemir.poshtar.micronaut.it.config;


import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.dead.DeadPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.global.GlobalTestPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.basic.BasicMockPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.hierarchy.HierarchyFirstBehaviour;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.mock.hierarchy.HierarchySecondBehaviour;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.order.OrderFirstPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.order.OrderSecondPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.specific.SpecificPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.transactional.basic.fail.FailTransactionalPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.transactional.basic.success.TransactionalPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.transactional.mandatory.fail.FailMandatoryPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryPipeline;
import io.github.nikola_velemir.poshtar.micronaut.it.pipeline.deps.validate.ValidationBehaviour;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class PipelineFactory {

    @Singleton
    public PipelineConfiguration poshtarPipeline() {
        return new PipelineConfiguration()
                .add(OrderFirstPipeline.class)
                .add(GlobalTestPipeline.class)
                .add(OrderSecondPipeline.class)
                .add(SpecificPipeline.class)
                .add(ValidationBehaviour.class)
                .add(TransactionalPipeline.class)
                .add(SucceedForMandatoryPipeline.class)
                .add(FailMandatoryPipeline.class)
                .add(DeadPipeline.class)
                .add(FailTransactionalPipeline.class)
                .add(BasicMockPipeline.class)
                .add(HierarchyFirstBehaviour.class)
                .add(HierarchySecondBehaviour.class);

    }
}
