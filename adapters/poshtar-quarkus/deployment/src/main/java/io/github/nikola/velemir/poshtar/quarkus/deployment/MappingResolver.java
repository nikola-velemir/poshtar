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

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.ParameterizedType;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.nikola.velemir.poshtar.quarkus.deployment.ProcessorConstants.PIPELINE_BEHAVIOUR_CLASS_NAME;

/**
 * Helper class that is used to create handler and behaviour mappings.
 *
 * <p>
 * Class provides mechanisms to map notification/request types to their handlers, as well as bind behaviours to handlers creating request chains.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class MappingResolver {
    /**
     * Create handler to behaviour mappings, later used to build full {@link io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain}.
     *
     * @param handlerToRequest   handler to request mappings map
     * @param behaviourToRequest behaviour to request mappings map
     * @param idx                JBoss index
     * @return full map of handler to behaviour bindings.
     */
    public static @NonNull Map<String, List<String>> mapHandlerToBehaviours(Map<String, String> handlerToRequest, Map<String, String> behaviourToRequest, IndexView idx) {
        Map<String, List<String>> handlerToBehaviours = new LinkedHashMap<>();
        handlerToRequest.forEach((handlerName, requestName) -> {
            List<String> matching = behaviourToRequest.entrySet().stream()
                    .filter(e -> {
                        String supportedRequest = e.getValue();
                        if (supportedRequest == null) return true;
                        return isAssignableFrom(idx, supportedRequest, requestName);
                    })
                    .map(Map.Entry::getKey)
                    .toList();
            handlerToBehaviours.put(handlerName, matching);
        });
        return handlerToBehaviours;
    }

    private static boolean isAssignableFrom(IndexView idx, String superName, String subName) {
        if (superName.equals(subName)) return true;
        ClassInfo ci = idx.getClassByName(DotName.createSimple(subName));
        if (ci == null) return false;
        if (ci.superName() != null && isAssignableFrom(idx, superName, ci.superName().toString())) return true;
        return ci.interfaceNames().stream()
                .anyMatch(iface -> isAssignableFrom(idx, superName, iface.toString()));
    }

    /**
     * Resolves request to behaviour mapping
     *
     * @param idx JBoss index
     * @return request to behaviour map
     */
    public static Map<String, String> resolveBehaviourMap(IndexView idx) {
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

    /**
     * Maps requests/notifcations to their designated handlers
     * @param idx              JBoss index
     * @param handlerInterface Handler interface FQN ({@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler}/{@link io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler}
     * @param markerInterface  Marker interface of a request/notification ({@link io.github.nikola_velemir.poshtar.core.request.Request}/{@link io.github.nikola_velemir.poshtar.core.notification.Notification})
     * @return Request/Notification to designated handler map
     */
    public static Map<String, String> resolveHandlerMap(IndexView idx, String handlerInterface, String markerInterface) {
        Map<String, String> result = new LinkedHashMap<>();

        for (ClassInfo ci : idx.getAllKnownImplementations(DotName.createSimple(handlerInterface))) {
            for (org.jboss.jandex.Type iface : ci.interfaceTypes()) {
                if (!iface.name().toString().equals(handlerInterface)) continue;
                if (!(iface instanceof ParameterizedType pt)) continue;

                org.jboss.jandex.Type arg = pt.arguments().get(0);
                String argName = arg.name().toString();

                if (idx.getClassByName(DotName.createSimple(argName)) != null
                        && idx.getAllKnownImplementations(DotName.createSimple(markerInterface))
                        .stream()
                        .anyMatch(c -> c.name().toString().equals(argName))) {
                    result.put(ci.name().toString(), argName);
                    break;
                }
            }
        }

        return result;
    }
}
