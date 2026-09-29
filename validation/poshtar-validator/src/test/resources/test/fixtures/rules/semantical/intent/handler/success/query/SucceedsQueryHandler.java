package test.fixtures.rules.semantical.intent.handler.success.query;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;

@Handler
public class SucceedsQueryHandler implements QueryHandler<SucceedsQuery, String> {
    @Override
    public String handle(SucceedsQuery succeedsQuery) {
        return "";
    }
}
