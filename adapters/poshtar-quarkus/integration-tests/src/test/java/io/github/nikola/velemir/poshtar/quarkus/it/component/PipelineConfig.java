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

package io.github.nikola.velemir.poshtar.quarkus.it.component;

import groovy.lang.Singleton;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.dead.DeadPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.global.GlobalTestPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.order.OrderFirstPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.order.OrderSecondPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.specific.SpecificPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.basic.fail.FailTransactionalPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.basic.success.TransactionalPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.mandatory.fail.FailMandatoryPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.transactional.mandatory.success.SucceedForMandatoryPipeline;
import io.github.nikola.velemir.poshtar.quarkus.it.component.pipeline.deps.validate.ValidationBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@Singleton
public class PipelineConfig {
    @Produces
    @ApplicationScoped
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
                .add(FailTransactionalPipeline.class);

    }
}
