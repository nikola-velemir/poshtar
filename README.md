# PoshtaR - DI agnostic Mediator & Pipeline library

PoshtaR is a dependency-injection-agnostic implementation of the
**Mediator pattern** for Java.  
Inspired by the popular `.NET MediatR` library, PoshtaR provides a simple way to decouple request handling, apply middleware behaviors and structure an application around clear, testable use cases.

PoshtaR itself is designed to work with **different DI frameworks**:
- Spring
- Guice

In `.NET` ecosystem, `MediatR` library is widely adopted, and today is considered a de-facto standard when building a large enterprise application. 

As of writing this , `java` lacks a widely adopted, DI-agnostic mediator library, with `MediatR`- style pipelines.

Most `java` applications rely heavily on **AOP** concepts for cross-cutting concerns, but it is implicit and makes cross-cutting logic harder to trace and unit-test compared to explicit pipeline behaviors as seen in many other frameworks or languages. 

Many `java` applications tend to also heavily use service layering, which introduces tight coupling between components, increasing testing overhead.

PoshtaR aims to fill that gap by providing a clean, familiar, framework-independent Mediator implementation. It aims to provide a type-safe, DI-agnostic implementation that simplifies architecture and improves maintainability across `java` ecosystems.

---

## Motivation

Modern applications often suffer from:
- Business logic leaking across multiple layers
- Bloated service classes or fat ViewModels
- Tight coupling between components
- Difficulty applying cross-cutting logic (logging, validation, authorization)
- Hard to structure, understand, and mock unit test cases 

PoshtaR aims to solve this by introducing:
- A single abstraction (`Mediator`) for sending requests
- One handler per use case (commands/queries)
- Pluggable pipeline behaviors (COR)
- Zero, one, or many handlers that are subscribed to events/notifications of a certain kind
- Optional annotation-based or compile-time handler discovery
- Compile-time type safety

## Architectural Impact & Clean Code
PoshtaR aims to shift how Java applications are structured, strongly enforcing SOLID principles and enabling modern architectural patterns:

- Single Responsibility Principle (SRP): Traditional Java codebases often suffer from the "Fat Service" anti-pattern, where a single service class handles dozens of unrelated operations. PoshtaR replaces this by ensuring every use case has exactly one dedicated Handler. Each class has only one reason to change, improving maintainability.

- Open/Closed Principle (OCP): Through Pipeline Behaviors, cross-cutting concerns (such as logging, performance metrics, or transaction management) can be introduced globally without modifying a single line of existing handler logic. The system is open for extension but closed for modification.

- Dependency Inversion (DIP): Presentation layers (e.g., REST Controllers) no longer depend on concrete business services. They depend only on the Mediator abstraction. This achieves decoupling between the web layer and business logic.

- Vertical Slice Architecture & CQRS: PoshtaR naturally pushes developers away from N-Tier (layered) architectures toward feature-based organization. Commands, Queries, and their respective Handlers can be grouped by feature (Vertical Slices), reducing cognitive load and avoiding the effects common in horizontally layered systems.

## Core concepts

### Request/response pattern

Each request is paired with a handler for a singular use cases.
Every use case is represented as:
	-  **Request** (command or query)
	- **RequestHandler** (one per request)
	
In C#, MediatR library solves this as a pair of a classes that extend IRequest and IRequestHandler
	
	
```C#
//C# Equivalent for comparison
	
public sealed record CreateMeetingCommand(
	    string Name,
	    string Description,
	    TimeOnly StartTime,
	    TimeOnly EndTime,
	    DateOnly Date) : IRequest<Result>;
	    
internal class CreateMeetingHandler(IMeetingDbContext dbContext) : IRequestHandler<CreateMeetingCommand, Result>
	{
	    public async Task<Result> Handle(CreateMeetingCommand request, CancellationToken cancellationToken)
	    {
	        var startTime = request.StartTime;
	        var endTime = request.EndTime;
	        var date = request.Date;
	        
	        var meeting = Meeting.Create(request.Name, request.Description, startTime, endTime, date);
	        
	        await dbContext.Meetings.AddAsync(meeting,cancellationToken);
	        await dbContext.SaveChangesAsync(cancellationToken);
	        
	        return Result.Success();
	    }
	
	    
	}
```

Either thru reflection or thru an adapter, MediatR resolves which handler is able to solve  a request, once the request has been passed to the MediatR object.

```C#
Result result = mediator.send(command);
```
	
PoshtaR's `java` equivalent, conceptually, would look like:
	
```java
@Request
public record GetUserQuery(String id) implements Request<UserDto> {}
	
@RequestHandler
public class GetUserHandler implements RequestHandler<GetUserQuery, UserDto> {
	    
	    private final UserRepository repo;
	
	    public GetUserHandler(UserRepository repo) {
	        this.repo = repo;
	    }
	
	    @Override
	    public UserDto handle(GetUserQuery query) {
	        return repo.find(query.id());
	    }
	}
	
	UserDto user = mediator.send(new GetUserQuery("123"));
	
```
 
Internally, PoshtaR:

- Resolves the correct handler (via reflection or adapter)
- Executes the configured pipeline behaviors
- Invokes the handler
- Returns the result

### **Notifications**/Events

PoshtaR also supports notifications, which represent one-to-many events with no return value.  
A notification may have **zero, one, or multiple handlers**, and all of them will be executed when it is published.

Notification definition:
```java
public record UserCreatedNotification(String id) implements Notification { }


public class AuditLogHandler implements NotificationHandler<UserCreatedNotification> {
	@Override
	public void handle(UserCreatedNotification notification) {         
	System.out.println("Audit log for user " + 
	notification.id());     
	} 
}

public class SendWelcomeEmailHandler implements NotificationHandler<UserCreatedNotification> {
	@Override
	public void handle(UserCreatedNotification notification) {
	    System.out.println("Sending welcome email to " + notification.id());     
	} 
}
```

Publishing a notification:
```java
mediator.publish(new UserCreatedNotification("123"));
```

All handlers registered for `UserCreatedNotification` will be invoked automatically.

---
### **Pipeline Behaviors**

Pipeline behavior represents a piece of logic that is executed before or after a request is handled. In other words, behaviors wrap around handler execution. **Pipelines are related only to request handling, not notifications**. These behaviors can be used for logging, validation, authorization and authentication, etc. 

Example flow:
`Logging → Validation → Transaction → Handler`

Example implementation:

```java
public class LoggingBehavior implements PipelineBehavior {
	public <TRequest, TResponse> TResponse handle(TRequest request, Next<TResponse> next) {
		
		System.out.println("Handling " + request.getClass().getSimpleName());
		
		return next.invoke();
	}
}
```

Users may register any number of behaviors they desire.

Pipeline behaviors are wrapping the next delegate, building the chain from the inside out.

If we have behaviors:
`A B C`

Call structure would be:
`A( next = B( next = C( next = handler ) ) )`

So the final executed pipeline is:
`A → B → C → Handler`

### DI Agnosticism and Adapters

At core, PoshtaR itself is completely DI agnostic, therefore it can not rely on DI mechanism or gimmicks of specific ecosystem, e.g. `Spring`. Otherwise it would be framework specific library, which would limit its use and it won't scale.
	
To integrate with different environments, PoshtaR uses adapters:
- **Spring Boot**
	Uses reflection or Spring `ApplicationContext` to discover handlers.
- **Google Guice**
	Uses the `ServiceFactory` interface.

---

### **Compile-Time Safety via Annotation Processing**

To improve experience, PoshtaR includes an annotation processor that performs compile-time validation of request–handler bindings. Processor verifies that each request has exactly one matching handler, that response types align correctly, and that no ambiguous or missing bindings exist. Annotation processor ensures structural errors in the mediator pipeline are caught early.

---
## Publishing

The library will be available on **Maven Central**. PoshtaR's users will be able to integrate the library with their projects by referencing the library in `pom.xml` file.

Example:

groupId: io.github.velemir  
artifactId: poshtar-core  
version: 1.0.0

```xml
<dependency>
    <groupId>io.github.velemir</groupId>
    <artifactId>poshtar-core</artifactId>
    <version>1.0.0</version>
</dependency>
---
```
Once published, dependency tools like Maven or Gradle will be able to resolve PoshtaR directly from Maven Central.

## Technical challenges

Key technical challenges to face during an implementation:

- Designing generic interfaces for type-safe request/handler resolution    
- Implementing a flexible pipeline
- Resolving handlers dynamically across different DI frameworks
- Avoiding classpath scanning overhead through preprocessing
- Ensuring thread-safety and low allocation overhead
- Providing meaningful errors when handlers are missing or ambiguous

