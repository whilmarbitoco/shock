package org.whilmarbitoco.exception;

import org.whilmarbitoco.core.HttpException;

public abstract class GlobalException {

    public final void exceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof HttpException) {
                System.out.println(((HttpException) throwable).getCode());
                System.out.println(throwable.getMessage());
            }
            register();
        });
    }

    public abstract void register();
}
