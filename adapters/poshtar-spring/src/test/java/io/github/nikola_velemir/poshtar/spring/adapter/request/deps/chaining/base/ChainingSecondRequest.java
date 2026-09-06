package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.base;

import io.github.nikola_velemir.poshtar.core.request.Request;

public final class ChainingSecondRequest implements Request<String> {
    public final int id;

    public ChainingSecondRequest(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChainingSecondRequest that = (ChainingSecondRequest) o;
        return id == that.id;
    }
}
