package org.whilmarbitoco.core.http;
import org.whilmarbitoco.core.View;
import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.router.Router;

import java.io.*;
import java.net.*;

public class Server {

    private final int port;
    private final Router router;

    public Server(int port, Router router) {
        this.port = port;
        this.router = router;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null) return;
            String[] parts = requestLine.split(" ");
            if (parts.length != 3) return;

            String uriWithParams = parts[1];
            String uri;
            String params = "";

            int hasParam = uriWithParams.indexOf("?");
            if (hasParam != -1) {
                uri = uriWithParams.substring(0, hasParam);
                params = uriWithParams.substring(hasParam + 1);
            } else {
                uri = uriWithParams;
            }

            Request request = new Request(parts[0], uri);
            Response response = new Response(uri);

            try {
                request.setParams(params);

                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    String[] header = line.split(":");
                    request.addHeader(header[0], header[1]);
                }

                router.resolve(request, response);
            } catch (HttpException e) {
                View view = new View(e.getMessage());
                view.template("template/error.html");
                response.send(view.toString());
            } finally {
                out.write(response.toString().getBytes());
                out.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
