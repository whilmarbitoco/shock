package org.whilmarbitoco.core;

import org.whilmarbitoco.core.context.HttpContext;
import org.whilmarbitoco.core.http.Response;

public abstract class GlobalException {

    public final void exceptionHandler() {

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof HttpException) {
                Response response = HttpContext.getResponse();
                System.out.println("error");
                response.send(throwable.getMessage());
            }
            register(throwable);
        });
    }

    public abstract void register(Throwable throwable);
}
