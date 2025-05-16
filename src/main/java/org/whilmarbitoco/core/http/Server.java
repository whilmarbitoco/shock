package org.whilmarbitoco.core.http;
import org.whilmarbitoco.core.View;
import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.router.Router;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
            Response response = new Response();

            try {

//                Read headers
                request.setParams(params);
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    String[] header = decode(line).split(":");
                    request.addHeader(header[0], header[1]);
                }

//                Ready Body
                String content = request.getHeader("Content-Length");
                int contentLength = Integer.parseInt((content == null ? "0" : content).trim());
                StringBuilder body = new StringBuilder();
                if (contentLength > 0) {
                    char[] buffer = new char[1024];
                    int bytesRead;
                    int totalBytesRead = 0;

                    while (totalBytesRead < contentLength && (bytesRead = in.read(buffer, 0, Math.min(buffer.length, contentLength - totalBytesRead))) != -1) {
                        body.append(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                    }
                    request.setBody(decode(body.toString()).split("&"));
                }


                router.resolve(request, response);
            } catch (HttpException e) {
                View view = new View();
                view.template("template/error.html");

                String error = View.getStackTraceString(e);
                String render = view.render(e.getMessage(), Map.of("stack", error));
                response.send(render);
            } finally {
                out.write(response.toString().getBytes());
                out.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected String decode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

}
