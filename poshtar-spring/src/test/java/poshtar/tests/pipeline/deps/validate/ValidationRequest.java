package poshtar.tests.pipeline.deps.validate;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public record ValidationRequest(int payload) implements IRequest<Integer> {
}
