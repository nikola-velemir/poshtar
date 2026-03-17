package com.example.demo.pipeline;

import com.example.demo.user.features.register.command.RegisterCommand;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.core.types.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Behaviour
@Order(0)
public class RegisterBehaviour implements PipelineBehaviour<RegisterCommand, Unit> {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBehaviour.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Unit handle(RegisterCommand command, RequestDelegate<RegisterCommand, Unit> requestDelegate) {
        logger.info("Request for register arrived: {}", command.username());

        return requestDelegate.handle(command);
    }
}
