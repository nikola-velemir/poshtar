<p align="center">
  <a href="https://github.com/nikola-velemir/poshtar">
    <img src="../assets/logo.svg" width="120" alt="PoshtaR Logo" />
  </a>
</p>

# PoshtaR Core

PoshtaR Core is the engine powering the entire PoshtaR ecosystem. It is designed to be completely DI-agnostic, defining the heart of the Mediator pattern and Pipeline logic without relying on specific frameworks like Spring or Guice.

## Core Concepts

The Core module defines the primary pillars of the architecture:

- The Mediator (`Poshtar`): A central communication point that decouples the message sender from its executioner.

- Handlers: Usecases for event or request use. Every handler models a singular usecase.

- The Pipeline: A middleware system allowing you to intercept requests (Logging, Validation, Caching) before they reach the handler.

- The Registry: The mechanism that maps message types to their respective implementations at runtime.

## Installation

If you are building your own adapter or wish to use Poshtar without a DI framework:

```xml
<dependency>
    <groupId>io.github.nikola-velemir</groupId>
    <artifactId>poshtar-core</artifactId>
    <version>${poshtar.version}</version>
</dependency>
```

## Quick Start

### Requests

**1. Define a Request and Response**

Requests are simple POJOs or Records (recommended) implementing `Request<T>`, where `T` is the response type.

```Java
public record GetUserQuery(UUID userId) implements Request<UserDto> {}
```

**2. Implement a Handler**

The handler contains the business logic. It must implement `RequestHandler<TRequest, TResponse>`.

```Java
public class GetUserHandler implements RequestHandler<GetUserQuery, UserDto> {
    @Override
    public UserDto handle(GetUserQuery request) {
        // Logic to retrieve the user
        return new UserDto(request.userId(), "Nikola");
    }
}
```


### Notifications


**1. Define a Notification**

Notifications are simple POJOs or Records (recommended) implementing `Notification`.

```Java
public record UserSignedInNotification(UUID userId) implements Notification {}
```

**2. Implement a Handler**

The handler contains the business logic. It must implement `NotificationHandler<TNotification extends Notification>`.

```Java
public class UserSignedInNotificationHandler implements NotificationHandler<UserSignedInNotification> {
    @Override
    public void handle(UserSigendInNotification notification) {
        // Logic to perform when user signs in.
    }
}
```


### Pipeline Behaviors (Middleware)

Behaviors allow you to execute code before and after the handler.

```Java
public class LoggingBehaviour<TRequest extends Request<TResponse>, TResponse> 
    implements PipelineBehaviour<TRequest, TResponse> {

    @Override
    public TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> next) {
        System.out.println("Sending request: " + request.getClass().getSimpleName());
        return next.handle(request);
    }
}
```

## Why Core?

- Zero Dependencies: Core has no external dependencies other than the standard Java library.

- Type Safety: Leverages Java Generics to ensure Request and Response are always aligned at the code level.

- Extensibility: Provides the essential interfaces (`RequestRegistry`, `NotificationRegistry`) that adapters (Spring/Guice) use to wire PoshtaR into your favorite DI container.

- Performance: Minimal overhead. The pipeline is optimized to run with a minimal number of object allocations.