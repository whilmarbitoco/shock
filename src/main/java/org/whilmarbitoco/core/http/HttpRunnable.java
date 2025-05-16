package org.whilmarbitoco.core.http;

import org.whilmarbitoco.core.View;
import org.whilmarbitoco.core.exception.HttpException;

public interface HttpRunnable {
    String handle(Request request, Response response);
}
