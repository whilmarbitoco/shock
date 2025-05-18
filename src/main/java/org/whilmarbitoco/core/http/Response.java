package org.whilmarbitoco.core.http;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Response {

    private final Map<String, String> headers = new HashMap<>();
    private final StringBuilder body = new StringBuilder();
    private StringBuilder cookie = new StringBuilder();

    private String shockSession;
    private boolean handled = false;
    private MimeType mimeType = MimeType.HTML;
    private int statusCode = 200;

    public Response() {
        setDefaultHeaders();
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getShockSession() {
        return shockSession;
    }

    public void setShockSession(String sessionID) {
        this.shockSession = sessionID;
    }

    public void setCookie(String name, String cookie) {
        StringBuilder ck = new StringBuilder();
        ck.append("Set-Cookie:")
                .append(name)
                .append("=")
                .append(cookie)
                .append("; HttpOnly; Path=/")
                .append("\r\n");
        this.cookie.append(ck.toString());
        ck.setLength(0);
    }

    private void setDefaultHeaders() {
        String date = ZonedDateTime.now(TimeZone.getTimeZone("GMT").toZoneId())
                .format(DateTimeFormatter.RFC_1123_DATE_TIME);
        headers.put("Date", date);
        headers.put("Server", "wb2c0NET/1.0");
        headers.put("Connection", "close");
        headers.put("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.put("Pragma", "no-cache");
        headers.put("Expires", "0");
    }

    public boolean isHandled() {
        return handled;
    }

    public void send(String content) {
        body.append(content);
        handled = true;
    }

    public void contentType(MimeType mimeType) {
        this.mimeType = mimeType;
        headers.put("Content-Type", this.mimeType.getType());
    }

    public Response redirect(String path) {
        handled = true;
        setStatus(301);
        headers.put("Location", path);
        return this;
    }

    public Response redirect(String path, int status) {
        handled = true;
        setStatus(status);
        headers.put("Location", path);
        return this;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder responseBuilder = new StringBuilder();

        headers.put("Content-Length", String.valueOf(body.length()));
        responseBuilder.append("HTTP/1.1 ").append(statusCode).append("\r\n");
        responseBuilder.append(cookie.toString());

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            responseBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        responseBuilder.append("\r\n").append(body.toString());

        return responseBuilder.toString();
    }
}
