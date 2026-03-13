package poshtar.tests.pipeline.deps.global;

import org.example.core.request.IRequest;
import org.example.core.types.Unit;

public record GlobalPipelineTestRequest() implements IRequest<Unit> {
}
