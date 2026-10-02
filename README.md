# Restful-Booker API Test Automation Framework

A Java API test automation framework for [`restful-booker`](https://restful-booker.herokuapp.com/),
built with **REST Assured** + **TestNG**, Maven, and a layered architecture (config → model → client → test).

## 📁 Structure

```
restful-booker-api-tests/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/qa/restfulbooker/
│   │           ├── clients/          # API clients and reusable request methods
│   │           ├── models/           # Request/response data models
│   │           ├── config/           # API configuration and environment setup
│   │           └── utils/            # Shared utilities and test helpers
│   │
│   └── test/
│       ├── java/
│       │   └── com/qa/restfulbooker/
│       │       ├── auth/             # Authentication API tests
│       │       ├── booking/          # Booking CRUD API tests
│       │       └── healthcheck/      # API health-check tests
│       │
│       └── resources/
│           └── restful-booker.properties  # Test configuration / credentials
│
├── pom.xml                           # Maven dependencies and build configuration
├── README.md                         # Project documentation and test instructions
└── .gitignore                        # Git ignored files
```

---

## Prerequisites

- JDK 11+ (`java -version`)
- Maven 3.8+ (`mvn -version`) - or just use VS Code's bundled Maven support
- VS Code with these extensions:
  - **Extension Pack for Java** (Microsoft) - gives you the JDK/debugger/project support
  - **Maven for Java** (Microsoft) - usually bundled in the pack above
  - **Test Runner for Java** (Microsoft, bundled in the pack) - lets you click ▶ next to any
    `@Test` method in the editor gutter

## 🧪 Running from the command line

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
(the `allure-testng` + `aspectjweaver` wiring in `pom.xml` handles this, no extra code needed).

```bash
mvn allure:report   # generates static HTML into target/site/allure-maven-plugin
mvn allure:serve    # builds the report and opens it in your browser immediately
```

---

## 📋 Test Coverage

```

API Tests
├── Authentication
│   └── POST /auth                   # Generate authentication token
│
├── Booking
│   ├── GET /booking                 # Retrieve booking IDs
│   ├── GET /booking/{id}            # Retrieve booking details
│   ├── POST /booking                # Create a booking
│   ├── PUT /booking/{id}            # Update a booking
│   ├── PATCH /booking/{id}          # Partially update a booking
│   └── DELETE /booking/{id}         # Delete a booking
│
└── Health Check
    └── GET /ping                    # API availability check
```

    
Each test class creates its own booking(s) in `@BeforeClass`/`@BeforeMethod`, so classes are
independent and safe to run individually, in any order, or in parallel later if you want.


## A note on this being a public demo API

`restful-booker.herokuapp.com` is a shared, free-tier public demo API (used for QA demo by
people everywhere), so:

- It can be slow to "wake up" on the first request after idling, if the very first test
  in a run times out, just re-run.
- Data isn't isolated per user, other people's bookings exist alongside yours. That's why
  `TestDataFactory` generates randomized names/dates instead of hardcoding fixed ones,
  and why tests only ever assert on data they themselves created.
- Validation is intentionally loose (e.g. it accepts a `totalprice` of `0`), so a couple of
  assertions are written to reflect the API's actual documented behavior rather than an
  idealized one.
