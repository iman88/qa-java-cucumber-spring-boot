# /maintenance — Framework Maintenance

Use this skill to run tests, fix failures, or update dependencies for the `qa-java-cucumber-spring-boot` framework.

## Running Tests

```bash
mvn test                                       # full suite (both CucumberRunners, all tags)
mvn test -Dcucumber.filter.tags="@google"      # Bing search tests only
mvn test -Dcucumber.filter.tags="@visa"        # visa form tests only
mvn test -Dspring.profiles.active=remote       # remote Playwright browser
mvn test-compile                               # compile-only check
```

Always run `mvn test` and confirm **BUILD SUCCESS** before declaring any task complete.

## Diagnosing Failures

### Browser stays open / cleanup not running
- Check `CucumberHooks.cleanupBrowser()` in `src/test/java/com/qa/bdd/CucumberHooks.java`
- Scope lookup uses `"browserscope"` (all lowercase) — wrong casing returns null and skips cleanup
- `@After(order=0)` runs last among @After hooks — correct for teardown

### Tests run: 0
- Surefire must use TestNG provider (explicit `surefire-testng` plugin dependency in `pom.xml`)
- Include pattern must be `**/CucumberRunner.java` (file-style), not `com.qa.CucumberRunner`

### Playwright CLI kills Maven
- `exec:java` causes Maven to exit when `CLI.main()` calls `System.exit()`
- Fix: `maven-antrun-plugin` with `fork="true"` — see `pom.xml` `playwright-install` execution

### Thread-safety errors (TargetClosedError / "Cannot find command")
- `Playwright`, `Browser`, `BrowserContext`, `Page` must ALL be `@ThreadScopeBean`
- If any of these is a singleton, parallel scenarios will corrupt each other's connections

### Bing result count thresholds
- Current thresholds: selenium≥2, java≥5, spring≥3 (conservative for parallel load)
- If a count assertion flakes lower the threshold in `search.feature`

## Updating Dependencies

### Spring Boot version
```xml
<!-- pom.xml parent -->
<version>3.X.X</version>
```
After updating, run `mvn test` and check for deprecated API warnings.

### Playwright version
```xml
<playwright.version>X.XX.X</playwright.version>
```
Playwright auto-installs browsers on next `mvn test` via the antrun plugin. Clear `~/.cache/ms-playwright` if you want a clean reinstall.

### Cucumber / TestNG
```xml
<cucumber.version>X.XX.X</cucumber.version>
<testng.version>X.XX.X</testng.version>
```
After major Cucumber version bumps, check `@CucumberContextConfiguration` and `@DataProvider` compatibility.

### Check for outdated dependencies
```bash
mvn versions:display-dependency-updates
mvn versions:display-plugin-updates
```

## Adding New Page Objects

1. Create class in `src/main/java/com/qa/page/<area>/`
2. Annotate with `@Page` (full page) or `@PageFragment` (component)
3. Extend `Base` — gets `Page page` injected automatically
4. Add URL to `application.properties` if needed
5. Inject via `@LazyAutowired` in step definitions

## Adding New Cucumber Scenarios

1. Add scenario to the appropriate `.feature` file in `src/test/resources/features/`
2. Tag with `@all` (picked up by default runner) and a specific tag for targeted runs
3. Add step definitions to `src/test/java/com/qa/bdd/` — no `@Component` needed, Cucumber manages the lifecycle
4. Add hook logic to `CucumberHooks` if scenario needs custom setup/teardown