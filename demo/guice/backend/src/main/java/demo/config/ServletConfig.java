package demo.config;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import demo.logs.repository.LogRepository;
import demo.logs.repository.LogRepositoryImpl;
import demo.user.repository.UserRepository;
import demo.user.repository.UserRepositoryImpl;
import demo.user.service.PasswordService;
import demo.user.service.PasswordServiceImpl;
import org.glassfish.jersey.servlet.ServletContainer;
import org.nikola.velemir.poshtar.adapter.configuration.PipelineConfigurer;
import org.nikola.velemir.poshtar.guice.adatper.module.PoshtarGuiceModule;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServletConfig extends GuiceServletContextListener {
    public static Injector injector;

    @Override
    protected Injector getInjector() {
        // We create the injector using the module defined below
        injector = Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(ServletContainer.class).in(com.google.inject.Singleton.class);
                install(new JpaPersistModule("myAppUnit"));
                filter("/*").through(PersistFilter.class);

                Map<String, String> params = new HashMap<>();
                params.put("jakarta.ws.rs.Application", "demo.config.MyResourceConfig");
                serve("/api/*").with(ServletContainer.class, params);

                PipelineConfigurer configurer = PipelineConfig.providePipelineConfigurer();

                install(new PoshtarGuiceModule(configurer, "demo"));

                bind(UserRepository.class).to(UserRepositoryImpl.class);
                bind(PasswordService.class).to(PasswordServiceImpl.class);
                bind(PasswordEncoder.class)
                        .toInstance(new BCryptPasswordEncoder(10));
                bind(LogRepository.class).to(LogRepositoryImpl.class);
            }

            @Provides
            @Singleton
            public ExecutorService provideExecutorService() {
                return Executors.newFixedThreadPool(10);
            }

            // 3. Move the Provider INSIDE the Module so Guice registers it
            @Provides
            @Singleton
            public JavaMailSender provideJavaMailSender() {
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                mailSender.setHost("smtp.gmail.com");
                mailSender.setPort(587);
                mailSender.setUsername("event.planner.team25@gmail.com");
                mailSender.setPassword("zbyflfzkqpprdjbf");

                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                return mailSender;
            }

        });
        return injector;
    }
}
