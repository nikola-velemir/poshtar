package com.example.demo.config;

import com.example.demo.pipeline.GetBehaviour;
import com.example.demo.pipeline.LoggingBehaviour;
import com.example.demo.pipeline.RegisterBehaviour;
import org.nikola.velemir.poshtar.adapter.configuration.PipelineConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfiguration {
    @Bean
    public PipelineConfigurer configurePipelines(){
        return new PipelineConfigurer()
                .add(RegisterBehaviour.class)
                .add(LoggingBehaviour.class)
                .add(GetBehaviour.class);
    }
}
