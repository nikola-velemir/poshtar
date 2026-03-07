package com.example.demo.pipeline;

import com.example.demo.user.application.create.command.CreateUserCommand;
import org.example.core.annotations.PipelineBehaviour;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@PipelineBehaviour
@Order(0)
public class CreateBehaviour implements IPipelineBehaviour<CreateUserCommand, Void> {
    private static final Logger logger = LoggerFactory.getLogger(CreateBehaviour.class);

    @Override
    public Void handle(CreateUserCommand command, RequestDelegate<Void> requestDelegate) {
        logger.info("Stigao request za create: " + command.name());

        return requestDelegate.handle();
    }
}
