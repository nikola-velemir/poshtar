package com.example.demo.pipeline;

import com.example.demo.user.features.register.command.RegisterCommand;
import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@PipelineBehaviour
@Order(0)
public class RegisterBehaviour implements IPipelineBehaviour<RegisterCommand, Unit> {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBehaviour.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Unit handle(RegisterCommand command, RequestDelegate<RegisterCommand, Unit> requestDelegate) {
        logger.info("Request for register arrived: {}", command.username());

        return requestDelegate.handle(command);
    }
}
