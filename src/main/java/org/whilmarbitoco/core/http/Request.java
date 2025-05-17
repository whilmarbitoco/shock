package org.whilmarbitoco.core.http;

import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.session.SessionManager;
import org.whilmarbitoco.exception.InternalServerException;
import org.whilmarbitoco.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> params = new HashMap<>();
    private final Map<String, Object> body = new HashMap<>();

    private String shockSession;
    private final String method;
    private final String path;

    public Request(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public void setParams(String uriParam) throws HttpException {
        if (uriParam.isEmpty()) return;
        String[] paramPair = uriParam.split("&");

        try {
            for (String pairs : paramPair) {
                String[] pair = pairs.split("=");
                params.putIfAbsent(pair[0], pair[1]);
            }
        } catch (Exception e) {
            throw new NotFoundException("Parameter key not found");
        }
    }

    public void setBody(String[] params) {
        if (params.length < 1) return;

       try {
           for (String param : params) {
               String[] p = param.split("=");
               body.putIfAbsent(p[0], p[1]);
           }
       } catch (Exception e) {
       }
    }

    public String getShockSession() {
        return shockSession;
    }

    public void setShockSession(String shockSession) {
        this.shockSession = shockSession;
    }

    public Object getParam(String key) {
        return params.get(key);
    }

    public Object getBody(String key) {
        return body.get(key);
    }

    public void addHeader(String header, String content) {
        headers.putIfAbsent(header, content);
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public static String getCookie(Request req, String cookieName) {
        String cookies = req.getHeader("Cookie");
        if (cookies == null) return null;

        if (cookies.contains(cookieName) && !cookieName.contains(";")) {
            return cookies.split("=")[1];
        }

        String[] cookieArray = cookies.split("; ");
        for (String cookie : cookieArray) {
            String[] parts = cookie.split("=", 2);
            if (parts.length == 2 && parts[0].equals(cookieName)) {
                return parts[1];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(":").append(value).append("\n"));

        // Remove trailing space
        return sb.toString().trim();
    }
}
