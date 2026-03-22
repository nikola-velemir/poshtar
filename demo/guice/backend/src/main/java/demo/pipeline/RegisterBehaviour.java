package demo.pipeline;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import demo.logs.model.Log;
import demo.logs.repository.LogRepository;
import demo.user.features.register.command.RegisterCommand;
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
public class RegisterBehaviour implements PipelineBehaviour<RegisterCommand, Unit> {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBehaviour.class);
    private final LogRepository repository;

    @Override
    @Transactional
    public Unit handle(RegisterCommand command, RequestDelegate<RegisterCommand, Unit> requestDelegate) {
        logger.info("Request for register arrived: {}", command.username());
        repository.save(new Log(Instant.now(), "REGISTER"));
        return requestDelegate.handle(command);
    }
}
