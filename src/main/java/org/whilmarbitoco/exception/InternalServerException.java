package org.whilmarbitoco.exception;

import org.whilmarbitoco.core.HttpException;

public class InternalServerException extends HttpException {
    public InternalServerException(String message) {
        super(500, message);
    }
}
