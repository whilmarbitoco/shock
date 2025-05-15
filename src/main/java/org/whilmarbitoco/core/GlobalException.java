package org.whilmarbitoco.core;

import org.whilmarbitoco.core.context.ResponseContext;
import org.whilmarbitoco.core.http.Response;

public abstract class GlobalException {

    public final void exceptionHandler() {

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Response response = ResponseContext.get();
            if (throwable instanceof HttpException) {
                response.setHeader("gumana", "wowowow");
                response.send(throwable.getMessage());
                System.out.println("HTTP EXCEPTION:: " + thread.toString());
            }
            register(throwable);
        });
    }

    public abstract void register(Throwable throwable);
}
