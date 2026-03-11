package com.example.demo.pipeline;

import com.example.demo.user.features.register.command.RegisterCommand;
import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.types.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@PipelineBehaviour
@Order(0)
public class RegisterBehaviour implements IPipelineBehaviour<RegisterCommand, Unit> {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBehaviour.class);

    @Override
    public Unit handle(RegisterCommand command, RequestDelegate<Unit> requestDelegate) {
        logger.info("Stigao request za create: " + command.username());

        return requestDelegate.handle();
    }
}
