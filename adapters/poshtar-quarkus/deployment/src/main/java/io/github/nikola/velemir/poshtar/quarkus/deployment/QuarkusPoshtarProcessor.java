/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry.QuarkusNotificationRegistry;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry.QuarkusRequestRegistry;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.recorder.PoshtarRecorder;
import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.producer.QuarkusPoshtarProducer;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem;
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
/**
 * Processor class that discovers and registers PoshtaR components as beans.
 *
 * <p>
 * Class discovers Poshtar components through their annotations,
 * as well as making bindings and wrappings that will be delivered to runtime to finalize the wiring.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class QuarkusPoshtarProcessor {
    public static final DotName APPLICATION_SCOPED_DOTNAME = DotName.createSimple(ApplicationScoped.class.getName());
    public static final DotName HANDLER_ANNOTATION_DOT_NAME = DotName.createSimple(Handler.class.getName());
    public static final DotName BEHAVIOUR_ANNOTATION_DOT_NAME = DotName.createSimple(Behaviour.class.getName());
    /**
     * Creates a Quarkus feature named "poshtar-quarkus".
     */
    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
    /**
     * Registers Quarkus-specific registries and providers as additional CDI beans.
     *
     * @return the additional bean build item containing the PoshtaR runtime classes
     */
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
    /**
     * Discovers all request handlers, notification handlers, and behaviours within the application
     * index, maps their relationships, and records their initialization for runtime registry setup.
     *
     * @param index The combined Jandex index containing application classes and dependencies
     * @param recorder The runtime recorder used to pass initialization data to the execution context
     */
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void registerComponents(
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

    /**
     * Defines core PoshtaR annotations as bean-defining annotations, ensuring that any class
     * marked with them automatically becomes a CDI bean scoped to the application.
     *
     * @return A list of bean-defining annotation build items mapping PoshtaR targets to {@link ApplicationScoped}
     */
    @BuildStep
    List<BeanDefiningAnnotationBuildItem> defineBeans() {
        return List.of(
                new BeanDefiningAnnotationBuildItem(HANDLER_ANNOTATION_DOT_NAME, APPLICATION_SCOPED_DOTNAME),
                new BeanDefiningAnnotationBuildItem(BEHAVIOUR_ANNOTATION_DOT_NAME, APPLICATION_SCOPED_DOTNAME)
        );
    }
    @BuildStep
    UnremovableBeanBuildItem keepPipelineConfigurationUnremovable() {
        return UnremovableBeanBuildItem.beanTypes(
                DotName.createSimple(PIPELINE_CONFIGURATION_CLASS_NAME)
        );
    }


}