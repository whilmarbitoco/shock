package org.whilmarbitoco.core.http;
import org.whilmarbitoco.core.MiddlewareRegistry;
import org.whilmarbitoco.core.RouteHandler;
import org.whilmarbitoco.core.Router;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final int port;
    private final Router router;
    private final List<Middleware> globalMiddleware = new ArrayList<>();
    private final MiddlewareRegistry middlewares;

    public Server(int port, Router router, MiddlewareRegistry middleware) {
        this.port = port;
        this.router = router;
        this.middlewares = middleware;
    }


    public void useGlobal(List<Middleware> middleware) {
        globalMiddleware.addAll(middleware);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            // Read the request line
            String requestLine = in.readLine();
            if (requestLine == null) return;

            // Parse request
            String[] parts = requestLine.split(" ");
            if (parts.length != 3) return;

            String method = parts[0];
            String path = parts[1];

            Request request = new Request(method, path);
            Response response = new Response();

            // Apply middlewares
            for (Middleware middleware : globalMiddleware) {
                middleware.handle(request, response);
                if (response.isHandled()) break;
            }

            assertRoute(request.getMethod(), request.getPath(), request, response);

            // Send response
            out.write(response.toString().getBytes());
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException("Something went wrong");
        }
    }

    private void assertRoute(String method, String path, Request req, Response res) {
        RouteHandler handler = router.getRoutes().get(method.toUpperCase()).get(path);

        if (handler == null) {
            throw new RuntimeException(method + " /" + path + " not registered");
        }

        for (String m : handler.getMiddlewares()) {
            middlewares.getMiddleware(m).handle(req, res);
        }
        handler.getFunc().handle(req, res);
    }
}
