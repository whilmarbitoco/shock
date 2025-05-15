package org.whilmarbitoco.core.exception;

import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

public interface ExceptionHandler {

    void handler(Exception exception, Request request, Response response);
}
