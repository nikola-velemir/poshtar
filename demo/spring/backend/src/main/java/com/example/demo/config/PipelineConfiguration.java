package com.example.demo.config;

import com.example.demo.pipeline.LoggingBehaviour;
import com.example.demo.pipeline.RegisterBehaviour;
import nikola.velemir.poshtar.spring.adapter.configuration.PipelineConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfiguration {
    @Bean
    public PipelineConfigurer configurePipelines(){
        return new PipelineConfigurer()
                .add(RegisterBehaviour.class)
                .add(LoggingBehaviour.class)
                .add(LoggingBehaviour.class);
    }
}
