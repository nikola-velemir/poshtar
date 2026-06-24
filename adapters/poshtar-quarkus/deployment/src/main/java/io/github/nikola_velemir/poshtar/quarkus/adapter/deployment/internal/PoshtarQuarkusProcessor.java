package io.github.nikola_velemir.poshtar.quarkus.adapter.deployment.internal;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.quarkus.adapter.runtime.internal.producer.QuarkusPoshtarProducer;
import io.github.nikola_velemir.poshtar.quarkus.adapter.runtime.internal.registry.QuarkusNotificationRegistry;
import io.github.nikola_velemir.poshtar.quarkus.adapter.runtime.internal.registry.QuarkusRequestRegistry;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.arc.processor.BuiltinScope;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.jandex.DotName;

public class PoshtarQuarkusProcessor {
    private static final String FEATURE = "poshtar";
    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
    @BuildStep
    BeanDefiningAnnotationBuildItem handlerAnnotation() {
        return new BeanDefiningAnnotationBuildItem(
                DotName.createSimple(Handler.class.getName()),
                BuiltinScope.SINGLETON.getName()  // default scope when annotation is found
        );
    }

    @BuildStep
    BeanDefiningAnnotationBuildItem behaviourAnnotation() {
        return new BeanDefiningAnnotationBuildItem(
                DotName.createSimple(Behaviour.class.getName()),
                BuiltinScope.SINGLETON.getName()
        );
    }
    @BuildStep
    AdditionalBeanBuildItem runtimeBeans() {
        return AdditionalBeanBuildItem.builder()
                .setUnremovable()
                .addBeanClasses(
                        QuarkusPoshtarProducer.class.getName(),
                        QuarkusNotificationRegistry.class.getName(),
                        QuarkusRequestRegistry.class.getName()
                )
                .build();
    }
}
