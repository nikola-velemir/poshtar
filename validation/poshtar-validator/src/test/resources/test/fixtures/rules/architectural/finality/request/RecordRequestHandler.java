package fixtures.rules.architectural.finality.request;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class RecordRequestHandler implements RequestHandler<RecordRequest, Unit> {
    @Override
    public Unit handle(RecordRequest request) {
        return null;
    }
}
