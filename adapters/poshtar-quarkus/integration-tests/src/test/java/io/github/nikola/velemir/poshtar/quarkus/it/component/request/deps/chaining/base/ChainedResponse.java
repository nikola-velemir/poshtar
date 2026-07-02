package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.base;

public class ChainedResponse {
    private final String response;
    public ChainedResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
