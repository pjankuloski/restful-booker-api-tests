# Restful-Booker API Test Automation Framework

A Java API test automation framework for [`restful-booker`](https://restful-booker.herokuapp.com/),
built with **REST Assured** + **TestNG**, Maven, and a layered architecture (config → model → client → test).

> Note on tech stack: Selenium WebDriver drives *browsers*, not REST APIs, so it isn't part of
> this project. For pure API testing, REST Assured is the Java-world equivalent of Selenium —
> a fluent DSL purpose-built for HTTP requests/assertions — paired with TestNG as the runner,
> exactly like you'd pair Selenium + TestNG for UI tests.

## Tech stack

| Concern              | Tool                          |
|-----------------------|-------------------------------|
| HTTP / API assertions | REST Assured 5.4              |
| Test runner           | TestNG 7.9                    |
| JSON (de)serialization| Jackson Databind              |
| Build tool            | Maven                         |
| Reporting             | Allure (+ TestNG's own report)|
| Logging               | SLF4J (simple binding)        |

## Project structure

```
restful-booker-api-tests/
├── pom.xml
├── testng.xml                                 # master suite, defines run order
├── src/main/java/com/qa/restfulbooker/
│   ├── config/ConfigManager.java              # loads config.properties
│   ├── models/                                # request/response POJOs
│   │   ├── Booking.java
│   │   ├── BookingDates.java
│   │   ├── BookingResponse.java
│   │   ├── BookingId.java
│   │   ├── AuthRequest.java
│   │   └── AuthResponse.java
│   ├── clients/                               # one client per API resource
│   │   ├── AuthClient.java
│   │   ├── BookingClient.java
│   │   └── PingClient.java
│   └── utils/TestDataFactory.java             # randomized valid test data
├── src/test/java/com/qa/restfulbooker/
│   ├── base/BaseTest.java                     # base URI + shared auth token, once per suite
│   ├── listeners/TestListener.java            # console pass/fail logging
│   └── tests/
│       ├── PingTests.java
│       ├── AuthTests.java
│       ├── CreateBookingTests.java
│       ├── GetBookingTests.java
│       ├── UpdateBookingTests.java
│       ├── PartialUpdateBookingTests.java
│       └── DeleteBookingTests.java
└── src/test/resources/config.properties       # base.url, auth.username, auth.password
```

## Endpoint → test coverage

| Endpoint                              | Test class                     | Scenarios covered |
|----------------------------------------|---------------------------------|--------------------|
| `GET /ping`                            | `PingTests`                     | 200/201 health check |
| `POST /auth`                           | `AuthTests`                     | valid creds → token; invalid creds → `reason` |
| `POST /booking`                        | `CreateBookingTests`            | full payload, 3 data-driven payloads, missing optional field |
| `GET /booking`                         | `GetBookingTests`               | all ids, filter by firstname/lastname, filter by checkin/checkout |
| `GET /booking/:id`                     | `GetBookingTests`                | valid id, non-existent id → 404 |
| `PUT /booking/:id`                     | `UpdateBookingTests`            | valid token → full replace, invalid token → 403 |
| `PATCH /booking/:id`                   | `PartialUpdateBookingTests`     | valid token → partial fields update, invalid token → 403 |
| `DELETE /booking/:id`                  | `DeleteBookingTests`            | valid token → 201 + record gone, no token → 403 |

Each test class creates its own booking(s) in `@BeforeClass`/`@BeforeMethod`, so classes are
independent and safe to run individually, in any order, or in parallel later if you want.

## Prerequisites

- JDK 11+ (`java -version`)
- Maven 3.8+ (`mvn -version`) — or just use VS Code's bundled Maven support
- VS Code with these extensions:
  - **Extension Pack for Java** (Microsoft) — gives you the JDK/debugger/project support
  - **Maven for Java** (Microsoft) — usually bundled in the pack above
  - **Test Runner for Java** (Microsoft, bundled in the pack) — lets you click ▶ next to any
    `@Test` method in the editor gutter

## Running from the command line

```bash
# run the whole suite (uses testng.xml)
mvn clean test

# run a single test class
mvn test -Dtest=AuthTests

# run a single test method
mvn test -Dtest=AuthTests#testCreateTokenWithValidCredentials
```

Console output shows `STARTED / PASSED / FAILED` lines per test from `TestListener`, and Maven's
own summary at the end. Surefire also writes `target/surefire-reports/` (plain-text + XML).

## Generating the Allure report

Allure result data is written automatically to `target/allure-results` on every run
(the `allure-testng` + `aspectjweaver` wiring in `pom.xml` handles this — no extra code needed).

```bash
mvn allure:report   # generates static HTML into target/site/allure-maven-plugin
mvn allure:serve    # builds the report and opens it in your browser immediately
```

(`allure:serve` requires the Allure commandline to be resolvable — the Maven plugin downloads
it automatically the first time, so just make sure you have internet access when you run it.)

## A note on this being a public demo API

`restful-booker.herokuapp.com` is a shared, free-tier public demo API (used for QA practice by
people everywhere), so:

- It can be slow to "wake up" on the first request after idling — if the very first test
  in a run times out, just re-run.
- Data isn't isolated per user, other people's bookings exist alongside yours. That's why
  `TestDataFactory` generates randomized names/dates instead of hardcoding fixed ones,
  and why tests only ever assert on data they themselves created.
- Validation is intentionally loose (e.g. it accepts a `totalprice` of `0`), so a couple of
  assertions are written to reflect the API's actual documented behavior rather than an
  idealized one.

