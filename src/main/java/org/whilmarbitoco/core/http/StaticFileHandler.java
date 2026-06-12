package org.whilmarbitoco.core.http;

import org.whilmarbitoco.core.utils.Config;

import java.io.*;
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
        String relativePath = "public/" + path.replaceFirst("^/static/", "");
        Path filePath = Paths.get(basePath, relativePath).normalize();

        if (!filePath.startsWith(Paths.get(basePath).normalize())) {
            response.setStatus(403);
            response.send("Forbidden");
            return;
        }

        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            response.setStatus(404);
            response.send("Not Found");
            return;
        }

        try {
            byte[] content = Files.readAllBytes(filePath);
            String mimeType = Files.probeContentType(filePath);
            if (mimeType == null) {
                mimeType = URLConnection.guessContentTypeFromName(filePath.getFileName().toString());
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
