# CLAUDE.md — qa-java-cucumber-spring-boot

## Project Identity

Spring Boot 3.4 + Playwright Java 1.49 + Cucumber 7 + TestNG 7 test automation framework.  
Java 21. Custom "kelvin" framework layer in `src/main/java/com/qa/kelvin/`.

## Build & Test Commands

```bash
mvn test                                      # full Cucumber BDD suite
mvn test -Dcucumber.filter.tags="@google"     # search tests only
mvn test -Dcucumber.filter.tags="@visa"       # visa form tests only
mvn test -Dspring.profiles.active=remote      # remote Playwright browser
mvn test-compile                              # compile only, no browser opened
```

Playwright browsers are installed automatically by `maven-antrun-plugin` during `process-test-resources`.

## Architecture Rules

### Never break these invariants:
- **One Playwright per thread.** `Playwright`, `Browser`, `BrowserContext`, and `Page` are all `@ThreadScopeBean` (scope name: `"browserscope"`). The BrowserScope class is in `com.qa.kelvin.scope`.
- **Cleanup after every scenario.** `CucumberHooks.cleanupBrowser()` in `com.qa.bdd` must close BrowserContext → Browser → Playwright and call `scope.clearCurrentThread()` after each scenario. Without this the browser stays open forever.
- **Parallel runner uses 2 threads.** `CucumberRunner` in `com.qa` has `@DataProvider(parallel=true)` with `dataproviderthreadcount=2` in surefire.

### Scope name is lowercase `"browserscope"` (NOT `"browserScope"`).
Any lookup via `getBeanFactory().getRegisteredScope(...)` must use the lowercase form.

### `@ThreadScopeBean` = `@Bean @Scope("browserscope")`.
Do not use `@Scope("thread")` or Spring's built-in `SimpleThreadScope` directly.

### `destroyMethod` is intentionally omitted for Playwright beans.
BrowserScope does not invoke destruction callbacks. Lifecycle is managed by `CucumberHooks`.

## Page Object Conventions

- Page classes: `@Page` (meta-annotation: `@Component + @Scope("browserscope")`)
- Page fragments: `@PageFragment` (same)
- Inject `Page` from Playwright via `@Autowired protected Page page` in `Base`
- `Base.browserContext()` returns the thread-scoped `BrowserContext`
- Multi-window: annotate the page class with `@Window("Title Prefix")` — `WindowAspect` swaps `Base.page` before/after each method call

## Known Issues / Decisions

- **Google replaced by Bing** — Google's CAPTCHA blocked automated searches. `application.url=https://www.bing.com`. Search locators: `[name='q']` for box, `li.b_algo` for results, `#b_results` for wait.
- **Result count thresholds are conservative** — Bing returns fewer results per page than Google under parallel load. Feature thresholds: selenium≥2, java≥5, spring≥3.
- **exec:java was replaced by antrun** — `com.microsoft.playwright.CLI.main()` calls `System.exit()`, which killed Maven when run via `exec:java`. Fixed with `maven-antrun-plugin` using `fork="true"`.
- **surefire-testng provider is explicit** — `spring-boot-starter-test` pulls in JUnit 5, causing surefire to auto-select JUnitPlatformProvider. The `surefire-testng` dependency in the plugin forces TestNG.

## Test Naming

The search feature file still says "google site" in scenario names — this is intentional to preserve the original course narrative. Only the URL and locators changed.
