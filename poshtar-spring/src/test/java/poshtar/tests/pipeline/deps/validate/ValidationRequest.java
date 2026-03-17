package poshtar.tests.pipeline.deps.validate;


import org.nikola.velemir.poshtar.core.request.IRequest;

public record ValidationRequest(int payload) implements IRequest<Integer> {
}
