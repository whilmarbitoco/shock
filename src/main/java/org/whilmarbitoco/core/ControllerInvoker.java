package org.whilmarbitoco.core;

import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.exception.InternalServerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerInvoker {

    public static Object invoke(Class<?> clazz, String methodName, Request req, Response res) throws HttpException {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();

            Method method = findMethod(clazz, methodName);
            if (method == null) {
                throw new NoSuchMethodException("Method '" + methodName + "' with matching parameters not found in class '" + clazz.getName() + "'");
            }
            method.setAccessible(true);
            return method.invoke(instance, req, res);
        } catch (Exception e) {
          throw new InternalServerException(e.getMessage());
        }
    }

    public static Method findMethod(Class<?> clazz, String method) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(method) && m.getParameterCount() == 2) {
                return m;
            }
        }
        return null;
    }
}
