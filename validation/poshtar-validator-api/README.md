<p align="center">
  <a href="https://github.com/nikola-velemir/poshtar">
    <img src="../../assets/logo.svg" width="120" alt="PoshtaR Logo" />
  </a>
</p>

# PoshtaR Validator API

The Validator API allows you to override some architectural rules enforced by validations processor. While PoshtaR aims for strict architectural integrity, real-world edge cases sometimes require you to bypass specific rules. This library provides the supression annotations to do this safely.

## Installation
Add this dependency to your pom.xml. Since these are compile-time annotations, you can often set the scope to provided.

```xml
<dependency>
    <groupId>io.github.nikola-velemir</groupId>
    <artifactId>poshtar-validator-api</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

## Overriding the Rules
Sometimes the processor rules are a bit too tight. Use these annotations to tell the Annotation Processor: "I know what I'm doing."

**1. @SuppressOrphan**

By default, the PoshtaR validator will fail the build if it finds a `Request` without a corresponding `RequestHandler`.

Use Case: You are in the middle of a refactor, you dont want the processor to prolong your session.

**2. @SuppressDead**

The validator performs an AST (Abstract Syntax Tree) scan on your `PipelineBehaviour`. If it doesn't see a call to `requestDelegate.handle(request)`, it marks the pipeline as "dead" (short-circuited).

Use Case: You are intentionally short-circuiting a pipeline (e.g., a Cache behavior that returns a result without calling the next handler) and the processor is flagging a false positive.

## Best Practices

Use Sparingly: These annotations are "escapes." Overusing them defeats the purpose of the validation processor.

Source Only: These will not appear in your bytecode via reflection. They are strictly for the `poshtar-validator` at build time.