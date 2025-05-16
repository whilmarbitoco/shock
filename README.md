# `Shock 🚀` — Full-Stack Java MVC Web Framework

`Shock` is a lightweight, modular, and developer-centric MVC web framework for Java, designed to provide complete control over request handling, routing, and response processing. Built for simplicity and flexibility, `Shock` empowers you to create robust web applications with minimal overhead.

---

### Features:

* 🛠️ **Flexible Routing System:** Define `GET`, `POST`, `PUT`, and `DELETE` routes with ease.  The routing system is designed to be intuitive and allows for complex URL patterns.
* 🔄 **Request & Response Handling:** Provides convenient access to request data (headers, parameters, body) and facilitates the creation of dynamic and customized responses.
* ⚡ **Lightweight & Fast:** Focuses on essential components, minimizing overhead and maximizing performance.  Ideal for applications where speed and efficiency are critical.
* 📦 **Extendable Middleware Support:** Implement reusable middleware components for common tasks such as authentication, logging, request validation, and more.
* 📂 **MIME Type Management:** Includes built-in mappings for common content types and allows for easy addition of custom types.
* 🧩 **Modular Architecture:** Simple and organized MVC architecture promotes maintainability and scalability.
* ✅ **Developer-Friendly:** Designed to be easy to learn and use, allowing developers to quickly get up to speed and focus on building application logic.

---

### Getting Started:

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/whilmarbitoco/shock.git
    cd shock
    ```

2.  **Build the project:**

    ```bash
    javac -d out src/**/*.java
    ```

3.  **Run the sample server:**

    ```bash
    java -cp out org.whilmarbitoco.app.Main
    ```

---

### Core Concepts:

Shock is built around a few core concepts:

* **Routes:** Define how the application responds to different HTTP requests.  Routes are typically defined in a dedicated class.
* **Controllers:** Handle the logic for processing requests and generating responses.  Controllers interact with models and views.
* **Middleware:** Intercept requests before they reach the controller, allowing you to modify the request, handle authentication, or perform other common tasks.
* **Views:** Generate the HTML or other output that is sent to the client. Shock gives you the flexibility to use your preferred templating engine.

---

### Example Usage:

1.  **Register a route in `registry/Routes.java`:**

    ```java
      package org.whilmarbitoco.registry;
      
      public class Routes extends RouteRegistry {
      
          @Override
          public void register() {
              router.get("/", UserController::get);
          }
      }
    ```

2.  **Create a controller in `http/controller`:**

    ```java
      package org.whilmarbitoco.http.controller;

      public class UserController extends Controller {
      
          public static String get(Request request, Response response) {
              String name = (String) request.getParam("name");
      
              String view = "<h1>Welcome, {{name}}</h1>";
      
              return view().render(view, Map.of("name", name == null ? "Shock" : name, "year", LocalDate.now().getYear()));
          }
      }

    ```

3.  **Rendering a dynamic view:**

   * Create an HTML template (e.g., `index.html`) in your `views` directory.

   ```html  
        <!DOCTYPE html>
         <html>
            <head>
               <title>Welcome!</title>
            </head>
            <body>
               <h1>Hello, {{name}}!</h1>
               <p>Welcome to Shock Framework.</p>
            </body>
         </html>
   ```

   * Pass data to the view from your controller.

   ```java
        return view().render(view, Map.of("name", name == null ? "Shock" : name, "year", LocalDate.now().getYear()));
   ```

---

### License:

This project is licensed under the MIT License — see the [LICENSE](./LICENSE) file for details.

---

### Contributions:

Got a cool feature idea or found a bug?  We welcome contributions!  Please open an issue or submit a pull request.  Let's build something amazing together. 💪💻
