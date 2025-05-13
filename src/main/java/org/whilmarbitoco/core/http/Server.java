package org.whilmarbitoco.core.http;
import org.whilmarbitoco.core.Middleware;
import org.whilmarbitoco.core.RouteRegistry;
import org.whilmarbitoco.core.Router;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final int port;
    private final List<Middleware> middlewares = new ArrayList<>();
    private Router router;

    public Server(int port, Router router) {
        this.port = port;
        this.router = router;
    }


    public void use(Middleware middleware) {
        middlewares.add(middleware);
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
            for (Middleware middleware : middlewares) {
                middleware.handle(request, response);
                if (response.isHandled()) break;
            }

            router.assertRoute(request.getMethod(), request.getPath(), request, response);

            // Send response
            out.write(response.toString().getBytes());
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException("Something went wrong");
        }
    }
}
