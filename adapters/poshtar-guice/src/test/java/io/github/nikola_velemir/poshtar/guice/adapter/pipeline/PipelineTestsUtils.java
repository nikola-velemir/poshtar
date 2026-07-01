package io.github.nikola_velemir.poshtar.guice.adapter.pipeline;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import io.github.nikola_velemir.poshtar.guice.adapter.TestModule;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadPipelineCatcher;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead.DeadRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.global.GlobalTestPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order.OrderFirstPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order.OrderRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.order.OrderSecondPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.specific.SpecificPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail.FailTransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success.TransactionalPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.success.TransactionalRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationBehaviour;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.validate.ValidationRequestHandler;
import org.mockito.Mockito;

class PipelineTestsUtils {
    static void createSpies(Injector injector) {
        PipelineTests.globalPipeline = Mockito.spy(injector.getInstance(GlobalTestPipeline.class));
        PipelineTests.specificPipeline = Mockito.spy(injector.getInstance(SpecificPipeline.class));
        PipelineTests.deadPipeline = Mockito.spy(injector.getInstance(DeadPipeline.class));
        PipelineTests.deadPipelineCatcher = Mockito.spy(injector.getInstance(DeadPipelineCatcher.class));
        PipelineTests.deadRequestHandler = Mockito.spy(injector.getInstance(DeadRequestHandler.class));
        PipelineTests.orderFirstPipeline = Mockito.spy(injector.getInstance(OrderFirstPipeline.class));
        PipelineTests.orderSecondPipeline = Mockito.spy(injector.getInstance(OrderSecondPipeline.class));
        PipelineTests.orderRequestHandler = Mockito.spy(injector.getInstance(OrderRequestHandler.class));
        PipelineTests.validationRequestHandler = Mockito.spy(injector.getInstance(ValidationRequestHandler.class));
        PipelineTests.validationBehaviour = Mockito.spy(injector.getInstance(ValidationBehaviour.class));
        PipelineTests.failTransactionalPipeline = Mockito.spy(injector.getInstance(FailTransactionalPipeline.class));
        PipelineTests.failTransactionalHandler = Mockito.spy(injector.getInstance(FailTransactionalRequestHandler.class));
        PipelineTests.transactionalPipeline = Mockito.spy(injector.getInstance(TransactionalPipeline.class));
        PipelineTests.transactionalHandler = Mockito.spy(injector.getInstance(TransactionalRequestHandler.class));
    }

    static Injector buildTestInjector() {
        return Guice.createInjector(Modules.override(new TestModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(GlobalTestPipeline.class).toInstance(PipelineTests.globalPipeline);
                bind(SpecificPipeline.class).toInstance(PipelineTests.specificPipeline);
                bind(DeadPipeline.class).toInstance(PipelineTests.deadPipeline);
                bind(DeadPipelineCatcher.class).toInstance(PipelineTests.deadPipelineCatcher);
                bind(DeadRequestHandler.class).toInstance(PipelineTests.deadRequestHandler);
                bind(OrderRequestHandler.class).toInstance(PipelineTests.orderRequestHandler);
                bind(OrderSecondPipeline.class).toInstance(PipelineTests.orderSecondPipeline);
                bind(OrderFirstPipeline.class).toInstance(PipelineTests.orderFirstPipeline);
                bind(ValidationBehaviour.class).toInstance(PipelineTests.validationBehaviour);
                bind(ValidationRequestHandler.class).toInstance(PipelineTests.validationRequestHandler);
                bind(FailTransactionalRequestHandler.class).toInstance(PipelineTests.failTransactionalHandler);
                bind(FailTransactionalPipeline.class).toInstance(PipelineTests.failTransactionalPipeline);
                bind(TransactionalRequestHandler.class).toInstance(PipelineTests.transactionalHandler);
                bind(TransactionalPipeline.class).toInstance(PipelineTests.transactionalPipeline);
            }
        }));
    }
}
