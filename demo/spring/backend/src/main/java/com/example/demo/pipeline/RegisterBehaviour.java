package com.example.demo.pipeline;

import com.example.demo.shared.logs.model.Log;
import com.example.demo.shared.logs.repository.LogRepository;
import com.example.demo.user.features.register.command.RegisterCommand;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Behaviour
@RequiredArgsConstructor
public class RegisterBehaviour implements PipelineBehaviour<RegisterCommand, Unit> {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBehaviour.class);
    private final LogRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Unit handle(RegisterCommand command, RequestDelegate<RegisterCommand, Unit> requestDelegate) {
        logger.info("Request for register arrived: {}", command.username());
        repository.save(new Log(Instant.now(), "REGISTER"));
        return requestDelegate.handle(command);
    }
}
