package io.github.nikola.velemir.poshtar.quarkus.deployment;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.nikola.velemir.poshtar.quarkus.deployment.GenericsHelper.isAssignableFrom;
import static io.github.nikola.velemir.poshtar.quarkus.deployment.GenericsHelper.resolveGenericArgument;
import static io.github.nikola.velemir.poshtar.quarkus.deployment.ProcessorConstants.PIPELINE_BEHAVIOUR_CLASS_NAME;

class MappingResolver {

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

    public static Map<String, String> resolveBehaviourMap(IndexView idx) {
        Map<String, String> result = new LinkedHashMap<>();
        DotName behaviourInterfaceName = DotName.createSimple(PIPELINE_BEHAVIOUR_CLASS_NAME);

        for (ClassInfo ci : idx.getAllKnownImplementations(behaviourInterfaceName)) {
            org.jboss.jandex.Type arg = resolveGenericArgument(idx, ci, behaviourInterfaceName);
            if (arg == null) continue; // shouldn't happen, but be defensive
            result.put(ci.name().toString(),
                    arg.kind() == org.jboss.jandex.Type.Kind.TYPE_VARIABLE ? null : arg.name().toString());
        }

        return result;
    }

    public static Map<String, String> resolveHandlerMap(IndexView idx, String handlerInterface, String markerInterface) {
        Map<String, String> result = new LinkedHashMap<>();
        DotName handlerInterfaceName = DotName.createSimple(handlerInterface);
        DotName markerInterfaceName = DotName.createSimple(markerInterface);

        for (ClassInfo ci : idx.getAllKnownImplementations(handlerInterfaceName)) {
            org.jboss.jandex.Type arg = resolveGenericArgument(idx, ci, handlerInterfaceName);
            if (arg == null || arg.kind() == org.jboss.jandex.Type.Kind.TYPE_VARIABLE) continue;

            String argName = arg.name().toString();
            if (idx.getClassByName(DotName.createSimple(argName)) != null
                    && idx.getAllKnownImplementations(markerInterfaceName)
                    .stream()
                    .anyMatch(c -> c.name().toString().equals(argName))) {
                result.put(ci.name().toString(), argName);
            }
        }

        return result;
    }

}