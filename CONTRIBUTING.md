# Contributing to Shock

## Prerequisites

- Java 24
- Maven 3.9+
- Git

## Build

```bash
# Compile
mvn compile -P sqlite

# Run all tests
mvn test -P sqlite

# Run a single test class
mvn test -P sqlite -Dtest=RouterTest

# Run a single test method
mvn test -P sqlite -Dtest=RouterTest#pathParamResolution
```

## Running the Sample App

```bash
# Start MySQL and create the database
mysql -u root -e "CREATE DATABASE IF NOT EXISTS shock_test;"

# Edit src/main/resources/config.properties with your credentials

# Run migrations
mvn compile -P mysql -q && java -cp target/classes:$(echo ~/.m2/repository/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar) org.whilmarbitoco.Main --migrate

# Start the server
mvn compile -P mysql -q && java -cp target/classes:$(echo ~/.m2/repository/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar) org.whilmarbitoco.Main
```

## Project Conventions

### Package Layout

- `org.whilmarbitoco.core` — framework code. Must never import from `org.whilmarbitoco.http` or `org.whilmarbitoco.database` (app layer).
- `org.whilmarbitoco.registry` — route and middleware wiring (app layer).
- `org.whilmarbitoco.http` — controllers and application middleware.
- `org.whilmarbitoco.database` — models and repositories.

### Code Style

- No `@author` or `@since` tags in Javadoc.
- Javadoc on all public API methods and classes.
- Use `snake_case` for database column names (`@Column(name = "user_id")`), `camelCase` for Java fields.
- Test classes live in `src/test/java` under the same package as the class under test.
- Use real SQLite in-memory databases for tests. No `@patch` / `MagicMock` / `Mockito`. Simple fake classes with constructor injection.

### Annotations

All database annotations live in `org.whilmarbitoco.core.database`:

```java
@Table(name = "users")
public class User {
    @Primary
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @HasMany(foreignKey = "user_id", referencedTable = "posts", referencedColumn = "id")
    private List<Post> posts;

    @BelongsTo(foreignKey = "user_id", referencedTable = "users", referencedColumn = "id")
    private User user;
}
```

### Middleware

Implement `Middleware`, register in `Middlewares.java`:

```java
public class TimingMiddleware implements Middleware {
    @Override
    public void handle(Request request, Response response) {
        long start = System.currentTimeMillis();
        // middleware runs before the controller
        // call response.send() to short-circuit
    }
}
```

## Adding a Database Dialect

1. Create a class in `org.whilmarbitoco.core.database` implementing `Dialect`.
2. Register it in `Dialect.fromUrl()` and `Dialect.fromName()`.
3. Activate the corresponding Maven profile in `pom.xml`.
4. Add dialect-specific tests in `MigrationGeneratorDialectTest`.

The `Dialect` interface requires:

| Method | Purpose |
|--------|---------|
| `name()` | Lowercase dialect name (e.g. `"mysql"`) |
| `quoteIdentifier()` | Quote a table/column name (backticks for MySQL, double-quote for PG/SQLite) |
| `integerPKType()` | Type for integer primary keys |
| `longPKType()` | Type for long primary keys |
| `autoIncrement()` | Auto-increment keyword |
| `resolveType()` | Map Java type to SQL type |
| `foreignKeyConstraint()` | Generate FK constraint SQL |
| `createIndex()` | Generate CREATE INDEX SQL |
| `createTableIfNotExists()` | Generate CREATE TABLE IF NOT EXISTS |
| `tableEngine()` | Table engine clause (MySQL only, null for others) |
| `limitOffset()` | Pagination syntax |
| `typeMap()` | Map of Java types to SQL types |

## Adding Middleware

1. Create a class implementing `org.whilmarbitoco.core.http.Middleware`.
2. Register globally or by name in `Middlewares.java`.
3. Reference by name in route definitions: `router.get("/path", Controller.class, "method", "middlewareName")`.

## Testing

- All tests use **real SQLite in-memory databases** — no mocking frameworks.
- Fake classes extend the JDBC interfaces directly as inner classes.
- Every new feature needs corresponding tests.
- CI runs on push and PR to `main`/`master` via GitHub Actions.

## Commit Messages

Use conventional prefixes:

- `feat:` new feature
- `fix:` bug fix
- `refactor:` behavior-preserving code change
- `docs:` documentation only
- `test:` adding or fixing tests
- `ci:` CI/workflow changes
- `chore:` build, tooling, misc

## Pull Request Checklist

- [ ] All tests pass (`mvn test -P sqlite`)
- [ ] New features have tests
- [ ] README updated if user-facing behavior changed
- [ ] CONTRIBUTING updated if conventions changed
