# Poshtar Guice Adapter

The Guice Adapter provides automated component discovery and integration for Google Guice. It is designed for developers who prefer the explicit control and speed of Google Guice. It bridges the gap between your Guice Modules and the PoshtaR.

## Installation
Add the Guice adapter to your pom.xml:

```xml
<dependency>
    <groupId>io.github.nikola-velemir</groupId>
    <artifactId>poshtar-guice</artifactId>
    <version>${poshtar.version}</version>
</dependency>
```

## Configuration

**1. Install the PoshtarGuiceModule**

To initialize PoshtaR, install the PoshtarGuiceModule within your main Guice module. You must provide a PipelineConfiguration and define the base packages for classpath scanning.

```java
public class MyApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        // 1. Define the execution order of global behaviors
        PipelineConfiguration pipeline = new PipelineConfiguration()
            .add(LoggingBehaviour.class)
            .add(ValidationBehaviour.class);

        // 2. Install the module and specify packages to scan for @Handler classes
        install(new PoshtarGuiceModule(pipeline, "com.mycompany.app.features"));
    }
}
```

**2. Automatic Component Discovery**

Unlike standard Guice configurations that require explicit bindings, the PoshtaR adapter automates the process:

Handlers: Any class annotated with @Handler within the specified base packages is automatically bound as a Singleton.

Behaviors: Classes registered in the PipelineConfiguration are automatically bound as Singleton instances to the Guice container.


## Usage

**Registering a Handler**

Mark your handler with the `@Handler` annotation. You can use standard Guice `@Inject` for constructor dependencies.

```java
@Handler
public class CreateUserHandler implements RequestHandler<CreateUserCommand, UserResponse> {

    private final UserRepository repository;

    @Inject
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
### Dispatching from a Component

Inject the `Poshtar` interface into your controllers or services. The adapter ensures the request travels through the managed pipeline to the resolved Guice singleton.

```java
public class UserApi {

    private final Poshtar poshtar;

    @Inject
    public UserApi(Poshtar poshtar) {
        this.poshtar = poshtar;
    }

    public void create(CreateUserCommand command) {
        UserResponse response = poshtar.send(command);
    }
}
```

## Static Validation

Even when using Guice’s runtime resolution, it is strongly recommended to use the `poshtar-validator` annotation processor. Processor will prevent architectural violations, helping you find errors early.