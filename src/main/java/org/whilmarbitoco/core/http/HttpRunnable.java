package org.whilmarbitoco.core.http;

import org.whilmarbitoco.core.view.View;

public interface HttpRunnable {
    String handle(Request request, Response response);
}
