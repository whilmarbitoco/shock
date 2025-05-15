package org.whilmarbitoco.core.exception;

import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public void handler(Exception exception, Request request, Response response) {

        if (exception instanceof HttpException) {

        }

    }
}
