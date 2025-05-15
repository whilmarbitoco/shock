package org.whilmarbitoco.core.http;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> params = new HashMap<>();

    private final String method;
    private final String path;

    public Request(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public void setParams(String uriParam) {
        if (uriParam.isEmpty()) return;
        String[] paramPair = uriParam.split("&");

        for (String pairs : paramPair) {
            String[] pair = pairs.split("=");
            params.putIfAbsent(pair[0], pair[1]);
        }

        System.out.println(params);
    }

    public Object getParam(String key) {
        return params.get(key);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(":").append(value).append("\n"));

        // Remove trailing space
        return sb.toString().trim();
    }
}
