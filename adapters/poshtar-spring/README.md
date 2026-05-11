# PoshtaR Spring Boot Adapter

The Spring Adapter allows you to leverage Spring for your PoshtaR components. By using the `@EnablePoshtar` annotation, the library automatically scans your project, registers your components, and prepares them for execution.

## Installation
Add the Spring adapter dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.github.nikola-velemir</groupId>
    <artifactId>poshtar-spring</artifactId>
    <version>${poshtar.version}</version>
</dependency>
```

## Configuration

**1. Enable PoshtaR**

To activate the library, add the `@EnablePoshtar` annotation to one of your configuration classes (usually the main `Application` class). This triggers the scanning process for `@Handler` and `@Behaviour` components.

```java
@SpringBootApplication
@EnablePoshtar(
// this can be ommited if you do not need to scan certain packages
basePackages = "com.yourdomain.features"
)

public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

**2. Define the Pipeline**

Order matters. In your Spring configuration, define a `PipelineConfiguration` bean to set the global execution order of your behaviors. Note that this is not mandatory for library to function, but it is highly recommended.

```java
@Configuration
public class PoshtarConfig {

    @Bean
    public PipelineConfiguration pipelineConfiguration() {
        return new PipelineConfiguration()
            .add(LoggingBehavior.class)
            .add(ValidationBehavior.class)
            .add(TransactionBehavior.class);
    }
}
```

## Usage

**Registering a Handler**

For a handler to be discoverable by Spring, it must be marked with PoshtaR `@Handler` annotation. For an example:

```java
@Handler
public class CreateUserHandler implements RequestHandler<CreateUserCommand, UserResponse> {

    private final UserRepository repository;

    public CreateUserHandler(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserResponse handle(CreateUserCommand request) {
        return repository.save(request.toEntity());
    }
}
```

**Registering a Pipeline Behaviour**

For a behaviour to be discoverable by Spring, it must be marked with PoshtaR `@Behaviour` annotation. For an example:

```java
@Behaviour
public class GlobalTestPipeline<TRequest extends Request<TResponse>, TResponse>
        implements PipelineBehaviour<TRequest, TResponse> {

    @Override
    public TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> requestDelegate) {
        System.out.println("Global pipeline called");
        return requestDelegate.handle(request);
    }
}
```

**Dispatching from a Controller**

Simply inject the `Poshtar` interface. The adapter ensures that when you call `send()`, the request travels through the managed pipeline to the correct bean.

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Poshtar poshtar;

    public UserController(Poshtar poshtar) {
        this.poshtar = poshtar;
    }

    @PostMapping
    public UserResponse create(@RequestBody CreateUserCommand command) {
        return poshtar.send(command);
    }
}
```

## Compile-Time Validation

Even in a Spring environment, you should use the PoshtaR validation processor. While Spring wires your beans at runtime, the validator checks your architecture at compile-time to ensure there are no architectural violations.

Refer to the root `README.md` for instructions on adding the `poshtar-validator` to your build plugins.