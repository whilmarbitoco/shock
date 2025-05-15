package org.whilmarbitoco.core.http;

public interface HttpRunnable {
    String handle(Request request, Response response);
}
