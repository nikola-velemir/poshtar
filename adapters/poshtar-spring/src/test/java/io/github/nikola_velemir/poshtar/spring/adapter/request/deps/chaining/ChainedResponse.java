package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining;

public class ChainedResponse {
    private final String response;
    public ChainedResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
