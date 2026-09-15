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
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.basic.BasicMockPipeline;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.basic.BasicMockRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.hierarchy.HierarchyFirstBehaviour;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.hierarchy.HierarchyRequestHandler;
import io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.hierarchy.HierarchySecondBehaviour;
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

/**
 * Shared test bootstrap helpers for the Poshtar pipeline test suites.
 * <p>
 * IMPORTANT: this class is intentionally NOT coupled to any specific
 * {@code PipelineTests} class. Earlier versions wrote directly to static
 * fields on one hardcoded {@code PipelineTests} class, which silently broke
 * any *other* test class reusing these helpers - their fields were never
 * populated and Guice failed with "Binding to null instances is not allowed".
 * <p>
 * Instead, {@link #createMocks()} and {@link #createSpies(Injector)} return
 * plain holder objects. Each calling test class copies the fields it needs
 * into its own static fields.
 */
public class PipelineTestsUtils {

    /** Plain Mockito mocks shared by a single test run. */
    public static class Mocks {
        public BasicMockPipeline basicMockPipeline;
        public HierarchySecondBehaviour hierarchySecondBehaviour;
    }

    /** Mockito spies wrapping real Guice-created pipeline/handler instances. */
    public static class Spies {
        public GlobalTestPipeline globalPipeline;
        public SpecificPipeline specificPipeline;
        public DeadPipeline deadPipeline;
        public DeadPipelineCatcher deadPipelineCatcher;
        public DeadRequestHandler deadRequestHandler;
        public OrderFirstPipeline orderFirstPipeline;
        public OrderSecondPipeline orderSecondPipeline;
        public OrderRequestHandler orderRequestHandler;
        public ValidationRequestHandler validationRequestHandler;
        public ValidationBehaviour validationBehaviour;
        public FailTransactionalPipeline failTransactionalPipeline;
        public FailTransactionalRequestHandler failTransactionalHandler;
        public TransactionalPipeline transactionalPipeline;
        public TransactionalRequestHandler transactionalHandler;
        public BasicMockRequestHandler basicMockrequestHandler;
        public HierarchyFirstBehaviour hierarchyFirstBehaviour;
        public HierarchyRequestHandler hierarchyRequestHandler;
    }

    public static Mocks createMocks() {
        Mocks mocks = new Mocks();
        mocks.basicMockPipeline = Mockito.mock(BasicMockPipeline.class);
        mocks.hierarchySecondBehaviour = Mockito.mock(HierarchySecondBehaviour.class);
        return mocks;
    }

    /**
     * Bootstrap injector used only to resolve real instances to wrap as spies.
     * Only the plain mocks are bound here (mirrors the original two-phase setup).
     */
    public static Injector buildBehaviourInjector(Mocks mocks) {
        return Guice.createInjector(Modules.override(new TestModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BasicMockPipeline.class).toInstance(mocks.basicMockPipeline);
                bind(HierarchySecondBehaviour.class).toInstance(mocks.hierarchySecondBehaviour);
            }
        }));
    }

    public static Spies createSpies(Injector behaviourInjector) {
        Spies spies = new Spies();
        spies.globalPipeline = Mockito.spy(behaviourInjector.getInstance(GlobalTestPipeline.class));
        spies.specificPipeline = Mockito.spy(behaviourInjector.getInstance(SpecificPipeline.class));
        spies.deadPipeline = Mockito.spy(behaviourInjector.getInstance(DeadPipeline.class));
        spies.deadPipelineCatcher = Mockito.spy(behaviourInjector.getInstance(DeadPipelineCatcher.class));
        spies.deadRequestHandler = Mockito.spy(behaviourInjector.getInstance(DeadRequestHandler.class));
        spies.orderFirstPipeline = Mockito.spy(behaviourInjector.getInstance(OrderFirstPipeline.class));
        spies.orderSecondPipeline = Mockito.spy(behaviourInjector.getInstance(OrderSecondPipeline.class));
        spies.orderRequestHandler = Mockito.spy(behaviourInjector.getInstance(OrderRequestHandler.class));
        spies.validationRequestHandler = Mockito.spy(behaviourInjector.getInstance(ValidationRequestHandler.class));
        spies.validationBehaviour = Mockito.spy(behaviourInjector.getInstance(ValidationBehaviour.class));
        spies.failTransactionalPipeline = Mockito.spy(behaviourInjector.getInstance(FailTransactionalPipeline.class));
        spies.failTransactionalHandler = Mockito.spy(behaviourInjector.getInstance(FailTransactionalRequestHandler.class));
        spies.transactionalPipeline = Mockito.spy(behaviourInjector.getInstance(TransactionalPipeline.class));
        spies.transactionalHandler = Mockito.spy(behaviourInjector.getInstance(TransactionalRequestHandler.class));
        spies.basicMockrequestHandler = Mockito.spy(behaviourInjector.getInstance(BasicMockRequestHandler.class));
        spies.hierarchyFirstBehaviour = Mockito.spy(behaviourInjector.getInstance(HierarchyFirstBehaviour.class));
        spies.hierarchyRequestHandler = Mockito.spy(behaviourInjector.getInstance(HierarchyRequestHandler.class));
        return spies;
    }

    public static Injector buildTestInjector(Mocks mocks, Spies spies) {
        return Guice.createInjector(Modules.override(new TestModule()).with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BasicMockRequestHandler.class).toInstance(spies.basicMockrequestHandler);
                bind(HierarchySecondBehaviour.class).toInstance(mocks.hierarchySecondBehaviour);

                bind(GlobalTestPipeline.class).toInstance(spies.globalPipeline);
                bind(SpecificPipeline.class).toInstance(spies.specificPipeline);
                bind(DeadPipeline.class).toInstance(spies.deadPipeline);
                bind(DeadPipelineCatcher.class).toInstance(spies.deadPipelineCatcher);
                bind(DeadRequestHandler.class).toInstance(spies.deadRequestHandler);
                bind(OrderRequestHandler.class).toInstance(spies.orderRequestHandler);
                bind(OrderSecondPipeline.class).toInstance(spies.orderSecondPipeline);
                bind(OrderFirstPipeline.class).toInstance(spies.orderFirstPipeline);
                bind(ValidationBehaviour.class).toInstance(spies.validationBehaviour);
                bind(ValidationRequestHandler.class).toInstance(spies.validationRequestHandler);
                bind(FailTransactionalRequestHandler.class).toInstance(spies.failTransactionalHandler);
                bind(FailTransactionalPipeline.class).toInstance(spies.failTransactionalPipeline);
                bind(TransactionalRequestHandler.class).toInstance(spies.transactionalHandler);
                bind(TransactionalPipeline.class).toInstance(spies.transactionalPipeline);
                bind(HierarchyFirstBehaviour.class).toInstance(spies.hierarchyFirstBehaviour);
                bind(BasicMockPipeline.class).toInstance(mocks.basicMockPipeline);
                bind(HierarchyRequestHandler.class).toInstance(spies.hierarchyRequestHandler);
            }
        }));
    }
}