package org.whilmarbitoco.exception;

import org.whilmarbitoco.core.exception.HttpException;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(404, message);
    }
}
