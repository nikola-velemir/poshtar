package demo.pipeline;

import com.google.inject.Inject;
import demo.logs.model.Log;
import demo.logs.repository.LogRepository;
import demo.user.features.activate.command.ActivateUserCommand;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

@Behaviour
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ActivationBehaviour implements PipelineBehaviour<ActivateUserCommand, Unit> {
    private static final Logger logger = LoggerFactory.getLogger(ActivationBehaviour.class);
    private final LogRepository repository;

    @Override
    public Unit handle(ActivateUserCommand command, RequestDelegate<ActivateUserCommand, Unit> delegate) {
        logger.info("Request for activation arrived: {}", command.username());
        repository.save(new Log(Instant.now(), "ACTIVATE"));
        return delegate.handle(command);    }
}
