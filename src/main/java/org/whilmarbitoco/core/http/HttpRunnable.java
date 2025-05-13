package org.whilmarbitoco.core.http;

public interface HttpRunnable {
    void handle(Request request, Response response);
}
