package org.whilmarbitoco.core.http;

import org.whilmarbitoco.core.utils.Config;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticFileHandler {

    private final String basePath;

    public StaticFileHandler() {
        this.basePath = Config.resources();
    }

    public void handle(String path, Response response) {
        String relativePath = path.replaceFirst("^/static/", "");
        Path filePath = Paths.get(basePath, relativePath).normalize();

        // Path traversal guard
        if (!filePath.startsWith(Paths.get(basePath).normalize())) {
            response.setStatus(403);
            response.send("Forbidden");
            return;
        }

        byte[] content;
        String mimeType;

        try {
            // Try filesystem first (development)
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                content = Files.readAllBytes(filePath);
                mimeType = Files.probeContentType(filePath);
            } else {
                // Fall back to classpath (JAR / production)
                String resourcePath = "public/" + relativePath;
                URL url = getClass().getClassLoader().getResource(resourcePath);
                if (url == null) {
                    response.setStatus(404);
                    response.send("Not Found");
                    return;
                }
                InputStream is = url.openStream();
                content = is.readAllBytes();
                is.close();
                mimeType = URLConnection.guessContentTypeFromName(resourcePath);
            }

            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setHeader("Content-Type", mimeType);
            response.setHeader("Content-Length", String.valueOf(content.length));
            response.send(new String(content, "UTF-8"));
        } catch (IOException e) {
            response.setStatus(500);
            response.send("Internal Server Error");
        }
    }
}
