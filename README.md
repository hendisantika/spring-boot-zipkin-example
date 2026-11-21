# spring-boot-zipkin-example

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-zipkin-example/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-zipkin-example/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-brightgreen?logo=springboot)
![Zipkin](https://img.shields.io/badge/Zipkin-3.6.1-blue)

Spring Boot Zipkin Example — two small Spring Boot services that call each other and report distributed traces to
[Zipkin](https://zipkin.io) using Micrometer Tracing with the OpenTelemetry bridge.

### Tech Stack

| Component         | Version / Library                                            |
|-------------------|--------------------------------------------------------------|
| Java              | 25 (Temurin)                                                 |
| Spring Boot       | 4.1.2                                                        |
| Spring Cloud      | 2025.1.4                                                     |
| Tracing           | Micrometer Tracing + `micrometer-tracing-bridge-otel`        |
| Exporter          | `opentelemetry-exporter-zipkin`                              |
| Metrics           | Micrometer Prometheus registry (`/actuator/prometheus`)      |
| Build             | Maven 3.9+                                                   |
| Tracing backend   | Zipkin (`openzipkin/zipkin:3.6.1` via Docker Compose)        |

### Project Structure

```
spring-boot-zipkin-example
├── .github
│   ├── dependabot.yml                    # Dependabot (Maven, GitHub Actions, Docker Compose)
│   └── workflows/maven.yml               # CI: build & test both services on JDK 25
├── docker-compose.yml                    # Zipkin server
├── spring-boot-zipkin-example-service-1  # port 8080, GET /test -> calls service-2
└── spring-boot-zipkin-example-service-2  # port 8081, GET /test
```

Request flow: `client -> service-1 (:8080/test) -> service-2 (:8081/test)`. Both hops share the same trace ID and are
reported to Zipkin at `http://localhost:9411/api/v2/spans`.

### Key Terminologies:

1. Trace: It can be used to represent the whole journey as it propagates through a distributed system and contains
   multiple spans.
2. Trace ID: Every trace contains one unique ID that can be named Trace ID, and it can be used to identify the traces
   uniquely in the Zipkin tracing service.
3. Span: It can represent a single unit of work within a trace, and it contains information that includes the operation
   of the request, timing information, metadata, and also the initial request and downstream calls.
4. Span ID: It can be used to uniquely identify the spans within a trace service, and all spans within the same trace
   share the same trace ID.
5. Parent Span ID: It can represent the initial span of the traces, and it can establish the hierarchy of spans in the
   Zipkin trace service.
6. Trace Context: It can contain the data of the Trace ID, Span ID, and parent Span ID that were propagated along with
   the request as a traversal through the different services.
7. Sampler: It is responsible for deciding which requests should be traced and which are not, and it helps to control a
   subset of requests.
8. Reporter: It can generate reports of the sending of span data to the tracing backend, and it can store and analyze
   the traces.
9. Zipkin UI Dashboard: It can represent the UI dashboard interface of the Zipkin server, and it can visualize traces,
   analyze performance, and identify issues with the systems.

### Standing Up Zipkin

Before instrumenting your code and collecting traces, you must first stand up an instance of Zipkin to which you can
send those collections. You have several options, all documented in
the [Zipkin Quickstart](https://zipkin.io/pages/quickstart.html), with the easiest being to run it on Docker. If you
have Docker running locally already, you can get Zipkin running with one command:

```shell
docker compose up -d
# or
docker run -d -p 9411:9411 openzipkin/zipkin:3.6.1
```

If you don’t have Docker locally but you do have Java installed, it’s almost as easy to run it on your machine. You can
download a pre-built JAR and run it with the following commands:

```shell
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar
```

Whichever way you choose to run Zipkin, you’ll find it accessible on your machine at http://127.0.0.1:9411. If you open
your browser to this address and see Zipkin running, you’re good to go!

### Prerequisites

- JDK 25
- Maven 3.9+
- Docker (optional, to run Zipkin)

### Things todo list

1. Clone this repository: `git clone https://github.com/hendisantika/spring-boot-zipkin-example.git`
2. Navigate to the folder: `cd spring-boot-zipkin-example`
3. Run the Zipkin Server: `docker compose up -d` (or `java -jar zipkin.jar`)
4. Run service 2 (port 8081):
   ```shell
   mvn -f spring-boot-zipkin-example-service-2/pom.xml clean spring-boot:run
   ```
5. Run service 1 (port 8080) in another terminal:
   ```shell
   mvn -f spring-boot-zipkin-example-service-1/pom.xml clean spring-boot:run
   ```
6. Trigger a traced request: `curl http://localhost:8080/test` (service 1 calls service 2)
7. Call service 2 directly: `curl http://localhost:8081/test`
8. Open Zipkin UI in your favorite browser: http://localhost:9411 and click **Run Query** to see the traces

### Build & Test

```shell
mvn -B verify -f spring-boot-zipkin-example-service-1/pom.xml
mvn -B verify -f spring-boot-zipkin-example-service-2/pom.xml
```

### CI & Dependency Updates

- **GitHub Actions** — [`maven.yml`](.github/workflows/maven.yml) builds and tests both services on JDK 25 (Temurin)
  using a matrix, on every push / pull request to `main`.
- **Dependabot** — [`dependabot.yml`](.github/dependabot.yml) keeps dependencies up to date:
    - Maven (both services, daily): grouped into `spring-boot`, `spring-cloud`, `observability`
      (Micrometer / OpenTelemetry / Zipkin), `maven-plugins` and `minor-and-patch`.
    - GitHub Actions (weekly, grouped into a single PR).
    - Docker Compose image for Zipkin (weekly).

### image Screenshots

Zipkin 1

![Zipkin 1](img/zipkin1.png "Zipkin 1")

Zipkin 2

![Zipkin 2](img/zipkin2.png "Zipkin 2")

Zipkin 3

![Zipkin 3](img/zipkin3.png "Zipkin 3")
