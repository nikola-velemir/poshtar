package org.nikola.velemir.poshtar.guice.adapter;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.persist.jpa.JpaPersistOptions;
import org.nikola.velemir.poshtar.adapter.configuration.PipelineConfigurer;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalPipeline;
import org.nikola.velemir.poshtar.guice.adatper.module.PoshtarGuiceModule;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.global.GlobalTestPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.order.OrderFirstPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.order.OrderSecondPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.specific.SpecificPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success.TransactionalPipeline;
import org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationBehaviour;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {

        PipelineConfigurer configurer = new PipelineConfigurer()
                .add(GlobalTestPipeline.class)
                .add(ValidationBehaviour.class)
                .add(OrderFirstPipeline.class)
                .add(OrderSecondPipeline.class)
                .add(SpecificPipeline.class)
                .add(DeadPipeline.class)
                .add(TransactionalPipeline.class)
                .add(FailTransactionalPipeline.class);
        install(new PoshtarGuiceModule(configurer, "org.nikola.velemir.poshtar.guice.adapter"));
        JpaPersistOptions options = JpaPersistOptions.builder()
                .setAutoBeginWorkOnEntityManagerCreation(true)
                .build();

        install(new JpaPersistModule("PoshtarUnit", options));
        bind(DbInitializer.class).asEagerSingleton();

    }

    static class DbInitializer {
        @Inject
        public DbInitializer(PersistService service) {
            service.start();
        }
    }
}
