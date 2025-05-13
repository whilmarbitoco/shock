# **`wb2c0Net🚀` — Minimalist Java MVC Web Framework** 🌐

`wb2c0Net` is a lightweight, modular, and developer-centric MVC web framework for Java, designed to provide complete control over request handling, routing, and response processing. Built for simplicity and flexibility, `wb2c0Net` empowers you to create robust web applications with minimal overhead.

---

### **Features:**

* 🛠️ **Custom Routing System:** Define GET, POST, PUT, DELETE routes effortlessly.
* 🔄 **Request & Response Handling:** Access request data and craft dynamic responses.
* ⚡ **Lightweight & Fast:** Focus on essential components without unnecessary bloat.
* 📦 **Extendable Middleware Support:** Create reusable middleware for authentication, logging, and more.
* 📂 **MIME Type Management:** Built-in mappings for common content types.
* 🧩 **Scalable Structure:** Simple and organized MVC architecture.

---

### **Getting Started:**

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/wb2c0Net.git
   cd wb2c0Net
   ```

2. Build the project:

   ```bash
   javac -d out src/**/*.java
   ```

3. Run the sample server:

   ```bash
   java -cp out org.whilmarbitoco.app.Main
   ```

---

### **Example Usage:**

```java
Router router = new Router();

router.get("/hello", (request, response) -> {
    response.setBody("Hello, wb2c0Net!");
    response.send();
});

router.post("/data", (request, response) -> {
    String body = request.getBody();
    response.setBody("Received: " + body);
    response.send();
});
```

---

### **License:**

This project is licensed under the MIT License — see the [LICENSE](./LICENSE) file for details.

---

### **Contributions:**

Got a cool feature idea or found a bug? Open an issue or submit a pull request. Let’s build something awesome together! 💪💻

---
