package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry.QuarkusNotificationRegistry;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry.QuarkusRequestRegistry;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.recorder.PoshtarRecorder;
import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.producer.QuarkusPoshtarProducer;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;

import java.util.List;
import java.util.Map;

import static io.github.nikola.velemir.poshtar.quarkus.deployment.ProcessorConstants.*;

class PoshtarQuarkusProcessor {
    public static final DotName APPLICATION_SCOPED_DOTNAME = DotName.createSimple(ApplicationScoped.class.getName());
    public static final DotName HANDLER_ANNOTATION_DOT_NAME = DotName.createSimple(Handler.class.getName());
    public static final DotName BEHAVIOUR_ANNOTATION_DOT_NAME = DotName.createSimple(Behaviour.class.getName());

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

        IndexView idx = index.getIndex();

        Map<String, String> handlerToRequest = MappingResolver.resolveHandlerMap(
                idx, REQUEST_HANDLER_CLASS_NAME, REQUEST_CLASS_NAME);

        Map<String, String> notificationHandlerToNotification = MappingResolver.resolveHandlerMap(
                idx, NOTIFICATION_HANDLER_CLASS_NAME, NOTIFICATION_CLASS_NAME);

        Map<String, String> behaviourToRequest = MappingResolver.resolveBehaviourMap(idx);
        Map<String, List<String>> handlerToBehaviours = MappingResolver.mapHandlerToBehaviours(handlerToRequest, behaviourToRequest, idx);
        recorder.initRegistries(handlerToRequest, notificationHandlerToNotification, handlerToBehaviours);
    }


    @BuildStep
    List<BeanDefiningAnnotationBuildItem> defineBeans() {
        return List.of(
                new BeanDefiningAnnotationBuildItem(HANDLER_ANNOTATION_DOT_NAME, APPLICATION_SCOPED_DOTNAME),
                new BeanDefiningAnnotationBuildItem(BEHAVIOUR_ANNOTATION_DOT_NAME, APPLICATION_SCOPED_DOTNAME)
        );
    }


}