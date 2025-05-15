package org.whilmarbitoco.exception;

import org.whilmarbitoco.core.exception.HttpException;

public class InternalServerException extends HttpException {
    public InternalServerException(String message) {
        super(500, message);
    }
}
