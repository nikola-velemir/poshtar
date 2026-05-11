<p align="center">
  <a href="https://github.com/nikola-velemir/poshtar">
    <img src="../../assets/logo.svg" width="120" alt="PoshtaR Logo" />
  </a>
</p>

# Poshtar Validator 

The PoshtaR Validator is a static analysis tool implemented as a Java Annotation Processor. It intercepts the compilation process to enforce architectural constraints and verify the structural integrity of the Mediator pattern implementation at compile-time.

## Installation

To integrate the validator into the build lifecycle, configure the `maven-compiler-plugin` to include the validator in the `annotationProcessorPaths`.

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>io.github.nikola-velemir</groupId>
                <artifactId>poshtar-validator</artifactId>
                <version>${poshtar.version}</version>
            </path>
            <path>
                <groupId>io.github.nikola-velemir</groupId>
                <artifactId>poshtar-validator-api</artifactId>
                <version>${poshtar.version}</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```


## Static Validation Rules

The processor executes the following validation logic during build phase of the project:

**1. Orhpaned Requests**

Every class implementing the `Request<T>` interface must have exactly one associated class annotated with @Handler that implements `RequestHandler<R, T>`.

Violation: Compilation error if a `Request` type lacks a matching `RequestHandler` implementation.

Override: Apply `@SuppressOrphan` to the Request type.

**2. Handler Ambiguity**

The library enforces a strict one-to-one mapping between a `Request` and its `RequestHandler`.

Violation: Compilation error if multiple `RequestHandler` implementations are detected for the same `Request` type.



**3. Interface and Annotation (Wiring) Consistency**

The processor validates that components are correctly categorized and do not possess conflicting definitions.

Constraint: A class annotated with `@Behaviour` cannot implement the `RequestHandler` interface.

Constraint: A class annotated with `@Handler` cannot implement the `PipelineBehaviour` interface.


**4. Single Responsibility Enforcement**

To prevent high coupling and maintain functional cohesion, a single class is prohibited from implementing multiple PoshtaR interfaces (e.g., a type cannot implement both `RequestHandler` and `NotificationHandler`).

**5. No direct injection**

To prevent from bypassing pipeline and mediator logic, it is prohibited to directly instantiate or inject PoshtaR components. Only mediator interface (`Poshtar`) can be injected for use.

**6. Pipeline Continuity (Dead Pipeline Detection)**

The processor performs an Abstract Syntax Tree (AST) analysis on all classes implementing `PipelineBehaviour`. It verifies that the handle method of the `RequestDelegate` is invoked within the method body to ensure the request chain is not unintentionally terminated.

Violation: Compilation error if the processor fails to detect a call to requestDelegate.handle(request).

Reasoning: Prevents "silent" failures where a middleware behavior stops the execution flow without a deliberate return or exception, leading to unreachable handlers.

Override: Apply `@SuppressDead` to the method if short-circuiting is the intended architectural behavior (e.g., a caching layer).

**7. Non-Primitive Response Types**

PoshtaR enforces the use of `Object` types for all Request-Response pairs. The generic parameter $T$ in Request<T> and RequestHandler<R, T> must be a reference type.

Violation: Warned if a primitive type (e.g., int, boolean, double) is used as a response type.

Reasoning: It is much more concise to have a return type that is a custom defined object, or collection of such objects, rather than a primitive type.

**8. Request Immutability and Finality**

To prevent side effects during pipeline execution, every class implementing the Request<T> or Notification interface must be declared as `final` or `record`.

Violation: Compilation error if a Request or Notification class is not marked with the `final` modifier, or if its not a `record`.

Reasoning: Request object travels through multiple behaviors before reaching the handler. Restricting inheritance ensures that the request structure is immutable and its behavior is predictable across the entire pipeline, preventing the "Fragile Base Class" problem within messaging.