# Java Learning Examples — Project Overview

This repository is a collection of Java examples ranging from basic to advanced topics.
Each Git branch corresponds to one example, solution, or sample project.

## Repository Structure

The `main` branch holds the `README.md` index. Each topic lives in its own branch and
subdirectory. Currently available branches/examples:

| Branch | Directory | Topic |
|--------|-----------|-------|
| `01.HelloWorld` | `hello-world/` | Spring Boot "Hello World" — entry point for the repo |
| `02.MemoryWithStack+Heap` | `stack+heap/` | JVM Stack vs Heap memory model |
| `03.ExceptionLambdaPipeline` | `exception+in+lambda/` | Exception propagation inside Java Stream pipelines |

---

## Examples

### 01. Hello World (`hello-world/`)

A minimal Spring Boot application (`@SpringBootApplication`) that implements
`CommandLineRunner` to print a greeting and exit. Used to verify the development
environment and demonstrate the basic project skeleton.

**Entry point:** `demo.helloworld.HelloWorldApplication`
**Build:** Gradle + Spring Boot 2.7.x, Java 11

---

### 02. Memory With Stack + Heap (`stack+heap/`)

Demonstrates how the JVM allocates memory across the Stack and Heap for:
- Primitive locals (`int i`) → Stack
- Object references (`Object obj`, `Memory mem`) → reference on Stack, object on Heap
- Method call frames and parameter passing (`foo(Object param)`)

Traces through a small call chain (`main` → `foo`) with annotated line numbers so the
reader can map each statement to its memory region.

**Entry point:** `stack_heap.Memory`
**Build:** Gradle + Spring Boot 2.7.x, Java 11

---

### 03. Exception in Lambda / Pipeline (`exception+in+lambda/`)

Explores how `RuntimeException` propagates through a Java Stream pipeline
(`filter → map → map → map → count`) under two execution models:

**Single-threaded (`stream()`):**
- All operations run on `main` thread.
- An exception thrown inside a `map()` stage immediately aborts the entire pipeline
  and bubbles up to the `try/catch` in `main`.
- Elements after the failing element are never processed.

**Multi-threaded (`parallelStream()` / ForkJoinPool):**
- Each element is processed on a worker thread.
- An exception in one worker's pipeline segment only breaks that segment; other
  workers continue processing their elements.
- After all workers finish, the exception is wrapped and re-thrown on the calling
  thread (`ForkJoinTask.invoke`), so `main` still sees it — but only after all
  other items have completed.

**Entry point:** `exception_in_lambda.MainApp`
**Build:** Gradle + Spring Boot 2.7.x, Java 11

---

## Development Environment

- **JDK:** 8 / 11 / 17
- **Build tools:** Gradle (per-module wrapper), Maven (optional)
- **IDE:** IntelliJ IDEA

## How to Run an Example

Each module has its own Gradle wrapper. From inside the module directory:

```bash
# e.g. for the exception-in-lambda example
cd "exception+in+lambda"
./gradlew bootRun
```

Or run the `main` class directly from IntelliJ IDEA.

## Branching Convention

```
<index>.<TopicName>   →  e.g. 03.ExceptionLambdaPipeline
```

Each branch is self-contained. Switch to the branch for the example you want to study,
then open the corresponding subdirectory in your IDE.
