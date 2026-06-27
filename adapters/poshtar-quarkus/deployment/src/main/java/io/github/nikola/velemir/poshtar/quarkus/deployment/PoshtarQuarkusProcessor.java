package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.github.nikola.velemir.poshtar.quarkus.runtime.PoshtarRecorder;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusNotificationRegistry;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusPoshtarProducer;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusRequestRegistry;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import org.jboss.jandex.DotName;

import java.util.List;

class PoshtarQuarkusProcessor {

    private static final String FEATURE = "poshtar-quarkus";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem runtimeBeans() {
        return AdditionalBeanBuildItem.builder()
                .setUnremovable()
                .addBeanClasses(
                        QuarkusPoshtarProducer.class.getName(),
                        QuarkusRequestRegistry.class.getName(),
                        QuarkusNotificationRegistry.class.getName()
                ).build();
    }

    @BuildStep
    AdditionalBeanBuildItem registerPoshtarComponents(CombinedIndexBuildItem index) {
        AdditionalBeanBuildItem.Builder builder = AdditionalBeanBuildItem.builder()
                .setUnremovable(); // Prevents ArC from optimizing away unused behaviors

        // 1. Register Handlers
        index.getIndex()
                .getAnnotations(DotName.createSimple(Handler.class.getName()))
                .stream()
                .map(annotation -> annotation.target().asClass().name().toString())
                .forEach(builder::addBeanClass);

        // 2. Register Behaviours
        index.getIndex()
                .getAnnotations(DotName.createSimple(Behaviour.class.getName()))
                .stream()
                .map(annotation -> annotation.target().asClass().name().toString())
                .forEach(builder::addBeanClass);

        builder.setDefaultScope(DotName.createSimple(ApplicationScoped.class.getName()));

        return builder.build();

    }
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerHandlersAndBehaviours(
            CombinedIndexBuildItem index,
            PoshtarRecorder recorder) {

        List<String> handlerClasses = index.getIndex()
                .getAnnotations(DotName.createSimple(Handler.class.getName()))
                .stream()
                .map(annotation -> annotation.target().asClass().name().toString())
                .toList();

        List<String> behaviourClasses = index.getIndex()
                .getAnnotations(DotName.createSimple(Behaviour.class.getName()))
                .stream()
                .map(annotation -> annotation.target().asClass().name().toString())
                .toList();

        recorder.registerHandlersAndBehaviours(handlerClasses,behaviourClasses);
    }
}