package io.github.nikola.velemir.poshtar.quarkus.deployment;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.List;

public final class HandlerClassesBuildItem extends SimpleBuildItem {
    private final List<String> classNames;

    public HandlerClassesBuildItem(List<String> classNames) {
        this.classNames = classNames;
    }

    public List<String> getClassNames() {
        return classNames;
    }
}
