package org.nikola.velemir.poshtar.opt.internal.rules;

import com.sun.source.util.Trees;
import org.nikola.velemir.poshtar.opt.internal.registry.RegistryEntry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;

public class RuleContext {
    public final ProcessingEnvironment env;
    public final Trees trees;
    private final Map<String, RegistryEntry> handlerRegistry = new LinkedHashMap<>();
    private final Set<String> knownRequests = new HashSet<>();

    public RuleContext(ProcessingEnvironment env, Trees trees) {
        this.env = env;
        this.trees = trees;
    }

    public void registerRequest(String requestFqn) {
        knownRequests.add(requestFqn);
    }

    public Map<String, RegistryEntry> getHandlerRegistry() {
        return Collections.unmodifiableMap(handlerRegistry);
    }

    public Elements getElements() {
        return env.getElementUtils();
    }
    public Types getTypes(){
        return env.getTypeUtils();
    }
    public RuleContext(ProcessingEnvironment env) {
        this.env = env;
        this.trees = Trees.instance(env);
    }

    public void registerHandler(String handlerFqn, String requestFqn,
                                Element handlerElement, AnnotationMirror mirror) {
        handlerRegistry.put(handlerFqn, new RegistryEntry(requestFqn, handlerFqn, handlerElement, mirror));
    }

    public void registerBehaviour(String behaviourFqn, Element behaviourElement, AnnotationMirror mirror) {
        handlerRegistry.put(behaviourFqn, new RegistryEntry("BEHAVIOUR", behaviourFqn, behaviourElement, mirror));

    }

    public Set<String> getHandledRequestTypes() {
        return handlerRegistry.values().stream()
                .filter(e -> !e.isBehaviour())
                .map(RegistryEntry::requestFQN)
                .collect(Collectors.toSet());
    }

    public Set<String> getKnownBehaviours() {
        return handlerRegistry.values().stream()
                .filter(RegistryEntry::isBehaviour)
                .map(RegistryEntry::handlerFQN)
                .collect(Collectors.toSet());
    }

    public Set<String> getAll() {
        return handlerRegistry.values().stream()
                .map(RegistryEntry::handlerFQN)
                .collect(Collectors.toSet());
    }

    public Set<String> getKnownRequests() {
        return Collections.unmodifiableSet(knownRequests);
    }

    public Properties toProperties() {
        Properties props = new Properties();
        handlerRegistry.values().forEach(e -> props.setProperty(e.handlerFQN(), e.requestFQN()));
        return props;
    }
}