package test.fixtures.rules.semantical.intent.handler.hierarchy.fail.query;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class FlagsHandler implements RequestHandler<FlagsQuery, String> {
    @Override
    public String handle(FlagsQuery flagsQuery) {
        return "";
    }
}
