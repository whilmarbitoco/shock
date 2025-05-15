package org.whilmarbitoco.core.http;

import org.whilmarbitoco.core.View;

public interface HttpRunnable {
    View handle(Request request, Response response);
}
