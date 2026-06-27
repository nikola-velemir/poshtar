package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.github.nikola.velemir.poshtar.quarkus.runtime.PoshtarRecorder;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusNotificationRegistry;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusPoshtarProducer;
import io.github.nikola.velemir.poshtar.quarkus.runtime.QuarkusRequestRegistry;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
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
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerHandlers(
            CombinedIndexBuildItem index,
            PoshtarRecorder recorder) {

        List<String> handlerClasses = index.getIndex().getAllKnownImplementations(DotName.createSimple(RequestHandler.class.getName()))
                .stream()
                .map(ci -> ci.name().toString())
                .toList();

        List<String> notificationHandlerClasses = index.getIndex().getAllKnownImplementations(DotName.createSimple(NotificationHandler.class.getName()))
                .stream()
                .map(ci -> ci.name().toString())
                .toList();


        recorder.initRegistries(handlerClasses, notificationHandlerClasses);
    }

    @BuildStep
    List<BeanDefiningAnnotationBuildItem> defineBeans() {
        return List.of(
                new BeanDefiningAnnotationBuildItem(DotName.createSimple(Handler.class.getName()), DotName.createSimple(ApplicationScoped.class.getName())),
                new BeanDefiningAnnotationBuildItem(DotName.createSimple(Behaviour.class.getName()), DotName.createSimple(ApplicationScoped.class.getName()))
        );
    }
}