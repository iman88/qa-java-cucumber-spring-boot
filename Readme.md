# Test Automation Framework — Spring Boot + Playwright

A **Cucumber / TestNG / Spring Boot** test automation framework demonstrating how Spring's IoC container, AOP, and custom scopes can serve as the backbone of a browser automation framework.

---

## Parent Project Attribution

This repository is a modernised fork of the sample code from the Udemy course  
**"SDET: Test Automation Framework [Java + Spring Boot]"** by **Vinoth Selvaraj (vIns)** — [vinsguru.com](https://vinsguru.com).

> Course slug: `cucumber-with-spring-boot` · Published June 2020 · Marked outdated 2024  
> The original GitHub repository has since been deleted by the author.

The original course used Selenium WebDriver + Java 17. This fork replaces Selenium with Playwright and updates the entire stack to Java 21 + Spring Boot 4.1.x while keeping the same architectural narrative.

---

## What the Framework Demonstrates

| Feature | Implementation |
|---|---|
| Per-thread browser lifecycle | Custom Spring scope (`BrowserScope` → `browserscope`) |
| Lazy component loading | `@LazyConfiguration`, `@LazyAutowired` |
| Page Object Model | `@Page`, `@PageFragment` annotations + AOP |
| Multi-window switching | `@Window` annotation + `WindowAspect` (AOP) |
| Screenshot on failure | `@TakeScreenshot` + `ScreenshotAspect` |
| Parallel Cucumber execution | `@DataProvider(parallel=true)`, 2 threads |
| Remote browser support | `application-remote.properties` + `RemoteBrowserConfig` |
| Fake test data | `net.datafaker` |
| In-memory DB | H2 + Spring Data JPA |

---

## Stack

| Component | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.2 |
| Playwright Java | 1.65.0 |
| Cucumber | 8.0.4 |
| TestNG | 7.12.0 |
| AssertJ | 3.27.7 |

---

## Project Structure

```
src/main/java/com/qa/
├── SpringSeleniumApplication.java
├── entity/
│   └── Customer.java
├── repository/
│   └── UserRepository.java
├── kelvin/                          # Framework internals
│   ├── annotation/
│   │   ├── LazyAutowired.java       # @Lazy @Autowired shorthand
│   │   ├── LazyConfiguration.java   # @Lazy @Configuration shorthand
│   │   ├── Page.java                # Marks a page object (@Component + browserscope)
│   │   ├── PageFragment.java        # Marks a page component (@Component + browserscope)
│   │   ├── TakeScreenshot.java      # Triggers screenshot via AOP
│   │   ├── ThreadScopeBean.java     # @Bean @Scope("browserscope")
│   │   └── Window.java              # Switches page context to named window via AOP
│   ├── aop/
│   │   ├── ScreenshotAspect.java    # Captures screenshot on @TakeScreenshot methods
│   │   └── WindowAspect.java        # Swaps Base.page before/after @Window methods
│   ├── config/
│   │   ├── FakerConfig.java         # net.datafaker.Faker bean
│   │   ├── PlaywrightConfig.java    # Playwright + Browser + BrowserContext + Page (all thread-scoped)
│   │   └── RemoteBrowserConfig.java # @Profile("remote") — connects to remote Playwright server
│   ├── scope/
│   │   ├── BrowserScope.java        # Custom Spring scope backed by ThreadLocal; clears stale contexts
│   │   ├── BrowserScopeConfig.java  # Registers BrowserScopePostProcessor as a bean
│   │   └── BrowserScopePostProcessor.java  # Registers "browserscope" with the bean factory
│   └── service/
│       ├── ScreenshotService.java   # Takes/returns PNG screenshots via the thread-scoped Page
│       └── WindowSwitchService.java # Resolves Page by title or index from BrowserContext
└── page/
    ├── Base.java                    # Abstract base: injects Page + ApplicationContext; exposes setPage()/defaultPage()
    ├── flights/
    │   └── FlightPage.java          # Nav-link test page (S3-hosted)
    ├── google/
    │   ├── GooglePage.java          # Bing search page (renamed from original course)
    │   ├── SearchComponent.java     # Search box fragment
    │   └── SearchResult.java        # Result list fragment
    ├── visa/
    │   └── VisaRegistrationPage.java
    └── window/
        ├── MainPage.java            # Opens child windows
        ├── PageA.java
        ├── PageB.java
        └── PageC.java              # Each annotated @Window("Page A/B/C")

src/test/java/com/qa/
├── CucumberRunner.java              # Main runner — @all tag, parallel=true (surefire entry point)
├── TestNGTest.java                  # Base class for TestNG tests
├── bdd/
│   ├── CucumberRunner.java          # Secondary runner — @visa tag
│   ├── CucumberHooks.java           # @After(order=0): closes browser + clears thread scope
│   ├── GoogleSteps.java             # Cucumber steps for search feature
│   └── VisaSteps.java               # Cucumber steps for visa feature
├── flights/                         # TestNG: Spring @Value + locale / i18n tests
│   ├── FlightAppDetails.java
│   ├── FlightTest.java
│   ├── EnTest.java
│   └── IdTest.java
├── resource/
│   └── ResourceTest.java
├── scope/                           # TestNG: Spring scope demonstration
│   ├── JuniorEngg.java
│   ├── SeniorEngg.java
│   ├── Salary.java
│   └── JuniorSeniorTest.java
├── testNG/
│   ├── googletest/
│   │   ├── Google1Test.java         # Full search + screenshot
│   │   ├── Google2Test.java
│   │   └── MultiBrowserTest.java    # Launches chromium + firefox in parallel
│   └── visa/
│       └── UserVisaTest.java
└── window/
    └── WindowSwitchTest.java        # @Window AOP cross-window test

src/test/resources/
├── features/
│   ├── search.feature               # @google — Bing search scenarios
│   └── visa-registration.feature   # @visa — form fill scenarios
├── language/
│   ├── en.properties
│   └── id.properties
├── data/testdata.csv
├── tables/user_visa.csv
└── data.sql
```

---

## Running the Tests

### Cucumber BDD suite (default)
```bash
mvn test
```

### Filter by tag
```bash
mvn test -Dcucumber.filter.tags="@google"
mvn test -Dcucumber.filter.tags="@visa"
mvn test -Dcucumber.filter.tags="@all"
```

### Remote browser (Playwright connect)
```bash
mvn test -Dspring.profiles.active=remote
```
Configure `playwright.remote.url` in `src/main/resources/application-remote.properties`.

### Headless mode
```bash
mvn test -Dbrowser.headless=true
```

---

## Key Configuration (`application.properties`)

```properties
application.url=https://www.bing.com
browser=chromium          # chromium | firefox | webkit
browser.headless=false
default.timeout=30000
screenshot.path=./logs
```

---

## Test Execution Notes

- Playwright browsers are installed automatically before tests via `maven-antrun-plugin`.
- Each parallel thread gets its own `Playwright` + `Browser` + `BrowserContext` + `Page` instance (Playwright Java is not thread-safe).
- `CucumberHooks.cleanupBrowser()` closes the browser and clears the thread scope after every scenario.
- The visa and window test pages are hosted on a public S3 bucket from the original course materials.

## 🔄 Maintenance & planned updates

> Versions in `pom.xml` are pinned, so nothing changes by itself, but pinned versions age. The table says when
> each component needs a look. Checked on 2026-11-13. To run the maintenance, ask Claude to run
> `/maintenance`.

| Component | Current version | End of life / event | Action needed | Urgency |
|---|---|---|---|---|
| Spring Boot | 4.1.2 | Free support for 4.1 until Jul 2027 (extended until Jul 2028) | Moved from 3.5 on 2026-11-13 together with Cucumber 8. Next: the 4.x line that is current when 4.1 ends; check `BrowserScope` and `@ThreadScopeBean` again, they hook into Spring's scope API | Before Jul 2027 |
| Cucumber | 8.0.4 | — | Needs a JSON library for its json/html reports: Jackson 3 (`tools.jackson.core:jackson-databind`, from Spring Boot). The runners override `scenarios(ITestContext)` for parallel runs | ✅ |
| Java | 21 LTS (local 21.0.11) | Supported until Dec 2029 | None for now. Java 25 LTS (until Sep 2031) is the next move; Spring Boot 4.1 supports 17-26 | ✅ |
| Maven | 3.9.2 (local) | — | 3.9.16 is out: update your local Maven | Next quarterly update |
| Playwright Java | 1.65.0 | Released about monthly | Move up every 3 months; browsers reinstall on the next `mvn test` | Within 3 months |
| TestNG | 7.12.0 | — | None | ✅ |
| maven-surefire-plugin + surefire-testng | 3.5.6 | — | Stay on 3.5.x: 3.6.x has no `surefire-testng` provider (runs TestNG on the JUnit Platform). Moving means a new runner setup | Plan |
| Other libraries | AssertJ 3.27.7, Datafaker 2.7.0, H2 and the Spring starters (managed by Spring Boot) | — | None | ✅ |
| CI | None (runs locally) | — | — | — |

## License

Copyright (c) 2024-2026 Ionut Man. My own changes (the Playwright, Java 21 and Spring Boot 3.4 migration and everything else added here) are under the [MIT License](LICENSE). The original course code and test pages by Vinoth Selvaraj stay with their author and are not covered by this licence. See [`LICENSE`](LICENSE).
