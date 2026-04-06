package org.nikola.velemir.poshtar.opt.rules;

import com.sun.source.util.Trees;
import org.nikola.velemir.poshtar.opt.processor.utils.registry.RegistryEntry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import java.util.*;
import java.util.stream.Collectors;

public class RuleContext {
    public final ProcessingEnvironment env;
    public final Trees trees;
    private final Map<String, RegistryEntry> registry = new LinkedHashMap<>();
    private final Set<String> knownRequests = new HashSet<>();

    public RuleContext(ProcessingEnvironment env, Trees trees) {
        this.env = env;
        this.trees = trees;
    }

    public void registerRequest(String requestFqn) {
        knownRequests.add(requestFqn);
    }

    public Map<String, RegistryEntry> getRegistry() {
        return Collections.unmodifiableMap(registry);
    }

    public Elements getElements() {
        return env.getElementUtils();
    }

    public RuleContext(ProcessingEnvironment env) {
        this.env = env;
        this.trees = Trees.instance(env);
    }

    public void registerHandler(String handlerFqn, String requestFqn,
                                Element handlerElement, AnnotationMirror mirror) {
        registry.put(handlerFqn, new RegistryEntry(requestFqn, handlerFqn, handlerElement, mirror));
    }

    public void registerBehaviour(String behaviourFqn, Element behaviourElement, AnnotationMirror mirror) {
        registry.put(behaviourFqn, new RegistryEntry("BEHAVIOUR", behaviourFqn, behaviourElement, mirror));

    }

    public Set<String> getHandledRequestTypes() {
        return registry.values().stream()
                .filter(e -> !e.isBehaviour())
                .map(RegistryEntry::requestFQN)
                .collect(Collectors.toSet());
    }

    public Set<String> getKnownBehaviours() {
        return registry.values().stream()
                .filter(RegistryEntry::isBehaviour)
                .map(RegistryEntry::handlerFQN)
                .collect(Collectors.toSet());
    }

    public Set<String> getAll() {
        return registry.values().stream()
                .map(RegistryEntry::handlerFQN)
                .collect(Collectors.toSet());
    }

    public Set<String> getKnownRequests() {
        return Collections.unmodifiableSet(knownRequests);
    }

    public Properties toProperties() {
        Properties props = new Properties();
        registry.values().forEach(e -> props.setProperty(e.handlerFQN(), e.requestFQN()));
        return props;
    }
}