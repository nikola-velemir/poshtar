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
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.ParameterizedType;

import java.util.LinkedHashMap;
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

        Map<String, String> handlerToRequest = resolveHandlerMap(
                idx, REQUEST_HANDLER_CLASS_NAME, REQUEST_CLASS_NAME);

        Map<String, String> notificationHandlerToNotification = resolveHandlerMap(
                idx, NOTIFICATION_HANDLER_CLASS_NAME, NOTIFICATION_CLASS_NAME);

        Map<String, String> behaviourToRequest = resolveBehaviourMap(idx);

        recorder.initRegistries(handlerToRequest, notificationHandlerToNotification, behaviourToRequest);
    }

    private Map<String, String> resolveBehaviourMap(IndexView idx) {
        Map<String, String> result = new LinkedHashMap<>();

        for (ClassInfo ci : idx.getAllKnownImplementations(DotName.createSimple(PIPELINE_BEHAVIOUR_CLASS_NAME))) {
            for (org.jboss.jandex.Type iface : ci.interfaceTypes()) {
                if (!iface.name().toString().equals(PIPELINE_BEHAVIOUR_CLASS_NAME)) continue;
                if (!(iface instanceof org.jboss.jandex.ParameterizedType pt)) continue;

                org.jboss.jandex.Type arg = pt.arguments().get(0);

                if (arg.kind() == org.jboss.jandex.Type.Kind.TYPE_VARIABLE) {
                    result.put(ci.name().toString(), null);
                } else {
                    result.put(ci.name().toString(), arg.name().toString());
                }
                break;
            }
        }

        return result;
    }
    private Map<String, String> resolveHandlerMap(IndexView idx, String handlerInterface, String markerInterface) {
        Map<String, String> result = new LinkedHashMap<>();

        for (ClassInfo ci : idx.getAllKnownImplementors(DotName.createSimple(handlerInterface))) {
            for (org.jboss.jandex.Type iface : ci.interfaceTypes()) {
                if (!iface.name().toString().equals(handlerInterface)) continue;
                if (!(iface instanceof ParameterizedType pt)) continue;

                org.jboss.jandex.Type arg = pt.arguments().get(0);
                String argName = arg.name().toString();

                if (idx.getClassByName(DotName.createSimple(argName)) != null
                        && idx.getAllKnownImplementors(DotName.createSimple(markerInterface))
                        .stream()
                        .anyMatch(c -> c.name().toString().equals(argName))) {
                    result.put(ci.name().toString(), argName);
                    break;
                }
            }
        }

        return result;
    }
    @BuildStep
    List<BeanDefiningAnnotationBuildItem> defineBeans() {
        return List.of(
                new BeanDefiningAnnotationBuildItem(HANDLER_ANNOTATION_DOT_NAME, APPLICATION_SCOPED_DOTNAME),
                new BeanDefiningAnnotationBuildItem(BEHAVIOUR_ANNOTATION_DOT_NAME, APPLICATION_SCOPED_DOTNAME)
        );
    }
}