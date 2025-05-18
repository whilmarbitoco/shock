package org.whilmarbitoco.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Error {
    public static String getStackTraceString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
