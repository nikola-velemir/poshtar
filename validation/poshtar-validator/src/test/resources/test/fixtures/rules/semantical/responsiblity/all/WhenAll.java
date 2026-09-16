package test.fixtures.rules.semantical.responsiblity.all;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
@Handler
public class WhenAll implements RequestHandler<WhenAllRequest, Unit>, NotificationHandler<WhenAllNotification>, PipelineBehaviour<WhenAllRequest, Unit> {
    @Override
    public Unit handle(WhenAllRequest request) {
        return null;
    }

    @Override
    public void handle(WhenAllNotification notification) {
        
    }

    @Override
    public Unit handle(WhenAllRequest request, RequestDelegate<WhenAllRequest, Unit> next) {
        return null;
    }
}
