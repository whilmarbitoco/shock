package org.whilmarbitoco.core.http;

import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.exception.InternalServerException;
import org.whilmarbitoco.exception.NotFoundException;
import org.whilmarbitoco.database.model.User;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> params = new HashMap<>();
    private final Map<String, Object> body = new HashMap<>();

    private String shockSession;
    private User auth;
    private final String method;
    private final String path;

    public Request(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public void setParams(String uriParam) throws HttpException {
        if (uriParam == null || uriParam.isEmpty()) return;
        String[] paramPair = uriParam.split("&");

        try {
            for (String pairs : paramPair) {
                String[] pair = pairs.split("=", 2);
                if (pair.length == 2) {
                    params.putIfAbsent(pair[0], pair[1]);
                }
            }
        } catch (Exception e) {
            throw new NotFoundException("Parameter key not found");
        }
    }

    public void setRouteParam(String key, String value) {
        params.put(key, value);
    }

    public void setBody(String[] params) {
        if (params == null || params.length < 1) return;

        try {
            for (String param : params) {
                String[] p = param.split("=", 2);
                if (p.length == 2) {
                    body.putIfAbsent(p[0], p[1]);
                }
            }
        } catch (Exception e) {
            throw new InternalServerException("Failed to parse request body: " + e.getMessage());
        }
    }

    public String getShockSession() {
        return shockSession;
    }

    public void setShockSession(String shockSession) {
        this.shockSession = shockSession;
    }

    public User getAuth() {
        return auth;
    }

    public void setAuth(User auth) {
        this.auth = auth;
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
        return sb.toString().trim();
    }
}
