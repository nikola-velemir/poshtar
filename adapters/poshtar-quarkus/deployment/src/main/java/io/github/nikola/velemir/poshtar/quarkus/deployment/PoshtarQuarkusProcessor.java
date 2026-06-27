package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusNotificationRegistry;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusPoshtarProducer;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusRequestRegistry;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
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
        index.getIndex().getAnnotations(DotName.createSimple(Handler.class.getName()))
                .forEach(ann -> builder.addBeanClass(ann.target().asClass().name().toString()));

        index.getIndex().getAnnotations(DotName.createSimple(Behaviour.class.getName()))
                .forEach(ann -> builder.addBeanClass(ann.target().asClass().name().toString()));

        // Force the scope explicitly on the builder
        builder.setDefaultScope(DotName.createSimple(ApplicationScoped.class.getName()));

        return builder.build();

    }
    @BuildStep
    List<BeanDefiningAnnotationBuildItem> defineBeans() {
        return List.of(
                new BeanDefiningAnnotationBuildItem(DotName.createSimple(Handler.class.getName()), DotName.createSimple(ApplicationScoped.class.getName())),
                new BeanDefiningAnnotationBuildItem(DotName.createSimple(Behaviour.class.getName()), DotName.createSimple(ApplicationScoped.class.getName()))
        );
    }
//    @BuildStep
//    @Record(ExecutionTime.RUNTIME_INIT)
//    void registerHandlersAndBehaviours(
//            CombinedIndexBuildItem index,
//            PoshtarRecorder recorder) {
//
//        List<String> handlerClasses = index.getIndex()
//                .getAnnotations(DotName.createSimple(Handler.class.getName()))
//                .stream()
//                .map(annotation -> annotation.target().asClass().name().toString())
//                .toList();
//
//        List<String> behaviourClasses = index.getIndex()
//                .getAnnotations(DotName.createSimple(Behaviour.class.getName()))
//                .stream()
//                .map(annotation -> annotation.target().asClass().name().toString())
//                .toList();
//
//        recorder.registerHandlersAndBehaviours(handlerClasses,behaviourClasses);
//    }
}