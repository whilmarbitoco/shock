# Shock — Java MVC Web Framework

Shock is a from-scratch Java MVC framework. No servlet container, no Spring, no Netty. Raw `ServerSocket` with routing, middleware, a custom template engine, and an Active Record ORM — all in a single repo with one external dependency (MySQL Connector).

## Feature Summary

- **HTTP Server**: Raw `ServerSocket` with multi-threaded request handling. Parses request line, headers, and body manually.
- **Routing**: Register `GET`, `POST`, `PUT`, `DELETE` routes with method references. URI query parameter parsing included.
- **Controllers**: Extend `Core.Controller`, define static handler methods that accept `Request` and `Response`.
- **Middleware**: Global and named middleware pipeline. Apply by name to specific routes.
- **Template Engine**: Hand-written lexer and recursive descent parser. Supports `{{variable}}`, `{% if condition %}`, `{% for var in list %}`. Layout-based — views inject into a base template.
- **Database**: `DBConnection`, query `Builder`, `Repository`, `Mapper`, and `EntityManager`. Table/Column/Primary annotations for model mapping.
- **Session**: `SessionManager` with session middleware for automatic session handling.
- **MIME Types**: Built-in content type mappings via `MimeType` utility.

### Architected for Flexibility

Built around a few core components that are designed to be extended or replaced:

- `org.whilmarbitoco.core` — framework source, never depends on app code
- `org.whilmarbitoco.registry` — wire your routes and middleware here
- `org.whilmarbitoco.http` — controllers and middleware go here
- `org.whilmarbitoco.database` — models and repositories
- `src/main/resources/config.properties` — server port, DB credentials, default view template

---

## Project Structure

```
src/main/java/org/whilmarbitoco/
├── Main.java                          # Entry point
├── core/
│   ├── Application.java               # Bootstraps routes, middleware, server
│   ├── Controller.java                # Base controller
│   ├── ControllerInvoker.java         # Invokes controller methods via reflection
│   ├── http/
│   │   ├── Server.java                # Raw ServerSocket, request parsing
│   │   ├── Request.java               # URI, params, headers, body
│   │   ├── Response.java              # Status, headers, body output
│   │   ├── Middleware.java            # Interface
│   │   └── MimeType.java              # Content-type mappings
│   ├── router/
│   │   ├── Router.java                # Route matching and resolution
│   │   ├── RouteHandler.java          # Handler metadata
│   │   └── RouteRegistry.java         # Base class for route definitions
│   ├── registry/
│   │   └── MiddlewareRegistry.java    # Global and named middleware
│   ├── view/
│   │   ├── View.java                  # Rendering + layout injection
│   │   ├── Lexer.java                 # Template tokenizer (regex-based)
│   │   ├── Parser.java                # Recursive descent parser
│   │   ├── Token.java / TokenType.java
│   │   └── node/                      # AST nodes: Text, Variable, If, For
│   ├── database/
│   │   ├── DBConnection.java          # Connection manager
│   │   ├── Builder.java               # Query builder
│   │   ├── Repository.java            # Generic repository
│   │   ├── Mapper.java                # ResultSet to object mapping
│   │   ├── EntityManager.java         # Entity operations
│   │   ├── Table.java / Column.java / Primary.java  # Annotations
│   │   └── QueryResult.java
│   ├── session/
│   │   └── SessionManager.java
│   ├── exception/
│   │   ├── HttpException.java
│   │   ├── ExceptionHandler.java
│   │   └── DefaultExceptionHandler.java
│   └── utils/
│       ├── Config.java                # Loads config.properties
│       ├── File.java                  # File read utility
│       └── Error.java                 # Stack trace formatting
├── registry/
│   ├── Routes.java                    # Route definitions
│   └── Middlewares.java               # Middleware registrations
├── http/
│   ├── controller/
│   │   ├── IndexController.java
│   │   ├── UserController.java
│   │   └── TodoController.java
│   └── middleware/
│       ├── AuthMiddleware.java
│       ├── SessionMiddleware.java
│       └── LogsMiddleware.java
└── database/
    ├── model/
    │   ├── User.java / Todo.java / Token.java
    └── repository/
        ├── UserRepository.java / TodoRepository.java / TokenRepository.java
```

---

## Getting Started

### Prerequisites
- Java 24
- Maven 3.x
- MySQL 8.x (for the sample app)

### 1. Clone
```bash
git clone https://github.com/whilmarbitoco/shock.git
cd shock
```

### 2. Configure
Edit `src/main/resources/config.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/your_db
db.user=your_user
db.password=your_password
server.port=8080
view.template=template.html
```

### 3. Build
```bash
mvn clean compile
```

### 4. Run
```bash
mvn exec:java -Dexec.mainClass="org.whilmarbitoco.Main"
```
Or compile and run manually:
```bash
javac -d out -cp "src/main/java:$(mvn dependency:build-classpath -q -DincludeScope=compile -Dmdep.outputFile=/dev/stdout)" $(find src/main/java -name "*.java")
java -cp "out:$(mvn dependency:build-classpath -q -DincludeScope=compile -Dmdep.outputFile=/dev/stdout)" org.whilmarbitoco.Main
```

The server starts on the configured port (default `8080`).

---

## Usage

### Define Routes

`src/main/java/org/whilmarbitoco/registry/Routes.java`:
```java
public class Routes extends RouteRegistry {
    @Override
    public void register() {
        router.get("/", IndexController.class, "get");
        router.get("/todo", TodoController.class, "viewTodo");
        router.post("/todo", TodoController.class, "addTodo");
    }
}
```

### Register Middleware

`src/main/java/org/whilmarbitoco/registry/Middlewares.java`:
```java
public class Middlewares extends MiddlewareRegistry {
    @Override
    public void global() {
        addGlobal(new LogsMiddleware());
        addGlobal(new SessionMiddleware());
    }

    @Override
    public void register() {
        add("auth", new AuthMiddleware());
    }
}
```

### Write a Controller

```java
public class IndexController extends Controller {
    public static String get(Request request, Response response) {
        return view().render("index.html", Map.of("name", "World"));
    }
}
```

### Create a View Template

`src/main/resources/view/index.html`:
```html
<!DOCTYPE html>
<html>
<head><title>Home</title></head>
<body>
    <h1>Hello, {{name}}!</h1>
    {% if name %}
        <p>Welcome back.</p>
    {% endif %}
</body>
</html>
```

The default layout (`template.html`) wraps views via `{{content}}` injection. Override with `view().template("other.html")` or pass raw HTML strings.

---

## Template Syntax

| Syntax | Description |
|--------|-------------|
| `{{variable}}` | Output a context variable |
| `{% if condition %}` | Conditional block |
| `{% endif %}` | Close conditional |
| `{% for item in list %}` | Loop over a collection |
| `{% endfor %}` | Close loop |

---

## Tech Stack

- **Java 24** (Maven)
- **MySQL Connector 8.0.33** (only external dependency)
- No servlet container, no third-party frameworks

## License

MIT — see [LICENSE](./LICENSE).
