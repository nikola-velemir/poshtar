package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.github.nikola.velemir.poshtar.quarkus.runtime.PoshtarRecorder;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusNotificationRegistry;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusPoshtarProducer;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusRequestRegistry;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Produce;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.pkg.builditem.ArtifactResultBuildItem;
import jakarta.inject.Singleton;
import org.jboss.jandex.DotName;

import java.util.List;

class PoshtarQuarkusProcessor {

    private static final String FEATURE = "poshtar-quarkus";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem registerHandlers(CombinedIndexBuildItem index) {
        List<String> handlerClasses = index.getIndex()
                .getAnnotations(DotName.createSimple(Handler.class.getName()))
                .stream()
                .map(annotation -> annotation.target().asClass().name().toString())
                .toList();

        System.out.println(">>> Found @Handler classes: " + handlerClasses);

        return AdditionalBeanBuildItem.builder()
                .setUnremovable()
                .addBeanClasses(handlerClasses)
                .build();
    }
    @BuildStep
    HandlerClassesBuildItem collectHandlers(CombinedIndexBuildItem index) {
        List<String> handlerClasses = index.getIndex()
                .getAnnotations(DotName.createSimple(Handler.class.getName()))
                .stream()
                .map(annotation -> annotation.target().asClass().name().toString())
                .toList();
        return new HandlerClassesBuildItem(handlerClasses);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerHandlers(HandlerClassesBuildItem handlers, PoshtarRecorder recorder) {
        recorder.registerHandlers(handlers.getClassNames());
    }
    @BuildStep
    AdditionalBeanBuildItem registerBehaviours(CombinedIndexBuildItem index) {
        List<String> behaviourClasses = index.getIndex()
                .getAnnotations(DotName.createSimple(Behaviour.class.getName()))
                .stream()
                .map(annotation -> annotation.target().asClass().name().toString())
                .toList();

        System.out.println(">>> Found @Behaviour classes: " + behaviourClasses);

        return AdditionalBeanBuildItem.builder()
                .setUnremovable()
                .addBeanClasses(behaviourClasses)
                .build();
    }
    @BuildStep
    @Produce(ArtifactResultBuildItem.class)
    void debugHandlers(CombinedIndexBuildItem index) {
        System.out.println(">>> PoshtarProcessor running");
        System.out.println(">>> @Handler classes found in index: " +
                index.getIndex()
                        .getAnnotations(DotName.createSimple(Handler.class.getName()))
                        .size());
    }

    @BuildStep
    AdditionalBeanBuildItem runtimeBeans() {
        return AdditionalBeanBuildItem.builder()
                .setUnremovable()
                .addBeanClasses(
                        QuarkusPoshtarProducer.class.getName(),
                        QuarkusRequestRegistry.class.getName(),
                        QuarkusNotificationRegistry.class.getName()
                )
                .build();
    }
}
