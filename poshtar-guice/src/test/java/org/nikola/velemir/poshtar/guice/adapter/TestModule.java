package org.nikola.velemir.poshtar.guice.adapter;

import com.google.inject.AbstractModule;
import org.nikola.velemir.poshar.guice.adatper.configuration.PipelineConfigurer;
import org.nikola.velemir.poshar.guice.adatper.module.PoshtarGuiceModule;
import org.nikola.velemir.poshtar.guice.adapter.request.deps.injection.DummyLoggingService;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {

        PipelineConfigurer configurer = new PipelineConfigurer();
        install(new PoshtarGuiceModule(configurer, "org.nikola.velemir.poshtar.guice.adapter")
        );
    }
}
