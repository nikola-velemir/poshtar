package test.fixtures.rules.semantical.intent.handler.hierarchy.success.query;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;

@Handler
public class SucceedsHandler implements QueryHandler<SucceedsQuery, String> {
    @Override
    public String handle(SucceedsQuery succeedsQuery) {
        return "";
    }
}
