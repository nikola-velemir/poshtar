package org.nikola.velemir.poshtar.opt.rules;

import com.sun.source.util.Trees;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleContext {
    public final ProcessingEnvironment env;
    public final Trees trees;
    private final Properties registry;
    private final Set<String> knownRequests = new HashSet<>();

    public RuleContext(ProcessingEnvironment env, Trees trees, Properties registry) {
        this.env = env;
        this.trees = trees;
        this.registry = registry;
    }

    public void registerRequest(String requestFqn) {
        knownRequests.add(requestFqn);
    }

    public Properties getRegistry() {
        return registry;
    }

    public Elements getElements() {
        return env.getElementUtils();
    }

    public RuleContext(ProcessingEnvironment env, Properties registry) {
        this.env = env;
        this.trees = Trees.instance(env);
        this.registry = registry;
    }

    public void registerHandler(String requestType, String handlerClass) {
        registry.setProperty(requestType, handlerClass);
    }

    public Set<String> getHandledRequestTypes() {
        return registry.entrySet().stream()
                .filter(e -> !"BEHAVIOUR".equals(e.getValue()))
                .map(e -> e.getValue().toString())
                .collect(Collectors.toSet());
    }

    public Set<String> getKnownBehaviours() {
        return registry.entrySet().stream()
                .filter(e -> "BEHAVIOUR".equals(e.getValue()))
                .map(e -> e.getKey().toString())
                .collect(Collectors.toSet());
    }

    public Set<String> getAll() {
        return registry.values().stream().map(Object::toString).collect(Collectors.toSet());
    }

    public Set<String> getKnownRequests() {
        return Collections.unmodifiableSet(knownRequests);
    }
}