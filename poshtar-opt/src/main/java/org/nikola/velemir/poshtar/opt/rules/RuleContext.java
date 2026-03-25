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

    public RuleContext(ProcessingEnvironment env, Properties registry) {
        this.env = env;
        this.trees = Trees.instance(env);
        this.registry = registry;
    }

    public void registerHandler(String requestType, String handlerClass) {
        registry.setProperty(requestType, handlerClass);
    }

    public Set<String> getAllKnownHandlers() {
        return registry.values().stream().map(Object::toString).collect(Collectors.toSet());
    }

    public String getHandlerFor(String requestType) {
        return registry.getProperty(requestType);
    }
}