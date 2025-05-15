package org.whilmarbitoco.core.http;
import org.whilmarbitoco.core.HttpException;
import org.whilmarbitoco.core.context.ResponseContext;
import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.core.RouteHandler;
import org.whilmarbitoco.core.Router;
import org.whilmarbitoco.exception.InternalServerException;
import org.whilmarbitoco.exception.NotFoundException;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
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

            String requestLine = in.readLine();
            if (requestLine == null) return;
            String[] parts = requestLine.split(" ");
            if (parts.length != 3) return;

            Request request = new Request(parts[0], parts[1]);
            Response response = new Response(parts[1]);
            ResponseContext.set(response);

            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                String[] header = line.split(":");
                request.addHeader(header[0], header[1]);
            }

            assertRoute(request.getMethod(), request.getPath(), request, response);

            out.write(ResponseContext.get().toString().getBytes());
            out.flush();

        } catch (IOException e) {
            throw new InternalServerException("Internal Server Error");
        } finally {
            ResponseContext.remove();
        }
    }

    private void assertRoute(String method, String path, Request req, Response res) {

        var methodRoutes = router.getRoutes().get(method.toUpperCase());
        if (methodRoutes == null) {
            throw new NotFoundException("Method " + method + " empty");
        }

        RouteHandler handler = methodRoutes.get(path);
        if (handler == null) {
            throw new NotFoundException("Path " + path + " not registered for method " + method);
        }

        try {
            for (String middlewareName : handler.getMiddlewares()) {
                Middleware middleware = middlewares.getMiddleware(middlewareName);
                if (middleware != null) {
                    middleware.handle(req, res);
                } else {
                    throw new InternalServerException("Middleware " + middlewareName + " not found.");
                }
            }

            handler.getFunc().handle(req, res);

        } catch (Exception e) {
            throw new InternalServerException("Error handling route " + method + " " + path);
        }
    }

}
