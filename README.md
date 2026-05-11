<p align="center">
  <a href="https://github.com/nikola-velemir/poshtar">
    <img src="./assets/logo.svg" width="120" alt="PoshtaR Logo" />
  </a>
</p>

# PoshtaR - DI agnostic Mediator & Pipeline library

PoshtaR is a dependency-injection-agnostic implementation of the
**Mediator pattern** for Java.  
Inspired by the popular `.NET MediatR` library, PoshtaR provides a way to decouple request handling, apply middleware behaviors and structure an application around clear, testable use cases.

PoshtaR itself is designed to work with **different DI frameworks**:
- Spring
- Guice

In `.NET` ecosystem, `MediatR` library is widely adopted, and today is considered a de-facto standard when building a large enterprise application. 

As of writing this , `java` lacks a widely adopted, DI-agnostic mediator library, with `MediatR`- style pipelines.

## Key Features

- DI Agnostic: Native support for Spring Boot, Google Guice, or vanilla Java.

- Request to Handler mappings: One request is handled by one and only one handler. Handlers represent a singular use case, preventing "Fat Services".

- Notifications: Events declared by users can be handled by multiple different handlers, broadcasting them.

- Pipeline Behaviors: Explicit Chain of Responsibility (CoR) for cross-cutting concerns like logging, validation, and transactions.

- Compile-Time Safety: Provided annotation processor verifies architectural rules when using the mediator pattern. It prevents common mistakes from leaking into runtime, causing late discovery of errors.

- Cleaner architecture: Encourages feature-based organization over the "Fat Service" anti-pattern.

- Minimal configuration overhead: Binding of components is declarative thru annotations. Developers are put on minimal strain when creating and wiring necessary components.

## Installation
To use the base implementation, add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.github.velemir</groupId>
    <artifactId>poshtar-core</artifactId>
    <version>{version}</version>
</dependency>
```

If you are using a specific framework for dependency injection, like **Spring** or **Guice**, you can include them instead:

```xml
<!-->Spring<-->
<dependency>
    <groupId>io.github.velemir</groupId>
    <artifactId>poshtar-spring</artifactId>
    <version>{version}</version>
</dependency>

<!-->Guice<-->
<dependency>
    <groupId>io.github.velemir</groupId>
    <artifactId>poshtar-guice</artifactId>
    <version>{version}</version>
</dependency>
```

For framework specific caveats, please refer to `README.md` files in designated framework folders under `adapters` folder (e.g `adapters/poshtar-spring`).


## Quick Start

### Requests and handlers

At base, all requests and handlers are defined the same, regardless of any DI framework.

**1. Define a Request**

Use a Java `record` or `final class` to ensure concise data transfer.

```java
public record YourRequest(String payload) implements Request<YourResponse>{}
```

All requests are to implement `Request<T>` interface.

**2. Create a handler**

Create a class, implementing `RequestHandler<YourRequest, YourResponse>`. Annotate your handler class with `@Handler` so it is discoverable by **PoshtaR**.

```java
@Handler
public class YourHandler implements RequestHandler<YourRequest, YourResponse> {
    /*
	Your repositories, services and other dependencies.
	*/

    public YourHandler(/*...*/) {
    }

    @Override
    public YourResponse handle(YourRequest request) {
		/*
		Your awesome business logic.
		*/
    }
}
```
**3. Send via Poshtar mediator**

```java
Poshtar poshtar = /*...*/
/*
...
*/
YourResponse response = poshtar.send(new YourRequest("What ever payload"));
```

### Notifications

**1. Define a Notification**

Use a Java `record` or `final class` to ensure concise data transfer.

```java
public record YourNotification(String payload) implements Notification{}
```

All notifications are to implement `Notification` interface.

**2. Create a handler, or multiple handlers**

Create a class, implementing `NotificationHandler<YourNotification>`. Annotate your handler class with `@Handler` so it is discoverable by **PoshtaR**.

```java
@Handler
public class YourHandler implements NotificationHandler<YourNotification> {
    /*
	Your repositories, services and other dependencies.
	*/

    public YourHandler(/*...*/) {
    }

    @Override
    public void handle(YourNotification notification) {
		/*
		Your awesome business logic.
		*/
    }
}
```
**3. Send via Poshtar mediator**

```java
Poshtar poshtar = /*...*/
/*
...
*/
poshtar.publish(new YourNotification("What ever payload"));
```

At base, all notifications and their handlers are defined the same, regardless of any DI framework. A singular notification can be handled by multiple notification handlers.

### Pipeline behaviors
Pipelines allow you to wrap custom logic around your handlers. This is perfect for logging, performance metrics, or transaction management.

Pipelines are considered either `global` (executing for every request regardless of a type), or `specific` (execute for specific request type, matching generic constraint).
.

**1. Define a behaviour**

Define a class implementing `PipelineBehaviour`, annotate behavior classes with `@Behaviour`, so they can be discovered by **PoshtaR**.

You are to call `handle` method on `RequestDelegate` in your overriden logic, to prevent short-circuited or "dead" pipelines.

```java
// Global loging behaviour
@Behaviour
public class LoggingBehavior <TRequest extends Request<TResponse>, TResponse>
        implements PipelineBehaviour<TRequest, TResponse> {

    @Override
    public TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> requestDelegate) {
        System.out.println("Global pipeline called");
        return requestDelegate.handle(request);
    }
}
/*
...
*/
// Specific behavior
@Behaviour
public class YourBehavior implements PipelineBehavior<YourRequest, YourResponse> {
    @Override
    public <YourRequest, YourResponse> YourResponse handle(YourRequest request, RequestDelegate<YourRequest, YourResponse> next) {
        System.out.println("Handling " + request.getClass().getSimpleName());
        return next.handle(request);
    }
}
```
**2. Pipeline configuration**

To define pipeline execution order, you can use `PipelineConfiguration` builder class, instantiating it and calling `add` method for every behaviour you define.

```java
PipelineConfiguration configuration = new PipelineConfiguration();

configuration
	.add(LoggingBehaviour.class)
	.add(YourPipelineBehaviour.class)
	/*...*/
	.add(TransactionBehaviour.class);
	
```
## The "Safety Net" (Annotation Processor)
Java applications often suffer from "implicit" logic that is hard to trace. PoshtaR introduces a Safety Net via its annotation processor:

- Missing Handler: If you define a `Request` but forget a `RequestHandler`, your build will fail with a clear error.

- Dead pipeline: If you forgot to call `handle` method on `RequestDelegate`, you will be warned that pipeline may end up "dead".

- Ambiguous handlers: If you define two or more handlers for the same request type, you violate one-to-one mapping rule between requests and handlers. Therefore, your build will fail.

- Direct injection: All requests and notification pass thru the mediator. Attempting to inject components (`RequestHandler`, `NotificationHanlder`, `PipelineBehaviour`) at your own will fail your build, because you attempt to bypass the entire mediator and pipeline logic. You may only inject `Poshtar` instances.

- Wiring: Your build will fail if you annotate a class as `@Behaviour` and it implements a `RequestHandler` interface (and vice versa). Your intent is ambiguous, causing a build to fail.

- Single Responsibility: A class implementing (e.g) a `RequestHandler`, may not implement `PipelineBehaviour` and `NotificationHandler`. This would violate a responsibility of a class, introducing multiple concerns for it to handle. Any variation between implementation of mentioned interfaces is forbidden.

- Request/Notification Finality: A class implementing `Request` or `Notification` may be `final class` or a `record`. This is to preserve immutability and forbid inheritance between classes recieved by a mediator.

`poshtar-core` itself does not validate these rules. To install a processor, and validate your architecture, you may add a build dependency in you `pom.xml`.

```xml

<build>
    <plugins>
        <plugin>
            <!-- ... -->
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.github.nikola-velemir</groupId>
                        <artifactId>poshtar-validator</artifactId>
                        <version>${version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>

    </plugins>
</build>
```

## Architectural Impact

PoshtaR helps you enforce SOLID principles without the boilerplate:

- SRP (Single Responsibility): Every use case has exactly one dedicated handler. No more 2,000-line service classes.

- OCP (Open/Closed): Add new features or behaviors without modifying existing handler code.

- DIP (Dependency Inversion): Controllers depend on the Mediator abstraction, not concrete business implementations.

- Feature based Architecture: Group your commands, queries, and handlers by feature rather than horizontal technical layers.

## Contributing and support

PoshtaR is in active development. If you are interested in seeing library grow in functionality or have suggestions, open a Github Issue.