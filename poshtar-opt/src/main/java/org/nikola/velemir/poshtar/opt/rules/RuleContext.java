package org.nikola.velemir.poshtar.opt.rules;

import com.sun.source.util.Trees;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleContext {
    public final ProcessingEnvironment env;
    public final Trees trees;
    private final Properties registry; // The persistent state

    public RuleContext(ProcessingEnvironment env, Trees trees, Properties registry) {
        this.env = env;
        this.trees = trees;
        this.registry = registry;
    }

    public Properties getRegistry() {
        return registry;
    }

    public RuleContext(ProcessingEnvironment env, Properties registry) {
        this.env = env;
        this.trees = Trees.instance(env);
        this.registry = registry;
    }

    public void registerHandler(String requestType, String handlerClass) {
        registry.setProperty(requestType, handlerClass);
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
}