package org.nikola.velemir.poshtar.guice.adapter;

import com.google.inject.AbstractModule;
import org.nikola.velemir.poshar.guice.adatper.configuration.PipelineConfigurer;
import org.nikola.velemir.poshar.guice.adatper.module.PoshtarGuiceModule;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.global.GlobalTestPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.order.OrderFirstPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.order.OrderSecondPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.specific.SpecificPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationBehaviour;
import org.nikola.velemir.poshtar.guice.adapter.request.deps.injection.DummyLoggingService;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {

        PipelineConfigurer configurer =new PipelineConfigurer()
                .add(GlobalTestPipeline.class)
                .add(ValidationBehaviour.class)
                .add(OrderFirstPipeline.class)
                .add(OrderSecondPipeline.class)
                .add(SpecificPipeline.class)
                .add(DeadPipeline.class);
        install(new PoshtarGuiceModule(configurer, "org.nikola.velemir.poshtar.guice.adapter")
        );
    }
}
