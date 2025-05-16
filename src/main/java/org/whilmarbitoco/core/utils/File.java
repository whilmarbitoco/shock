package org.whilmarbitoco.core.utils;

import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.exception.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class File {

    public static String loadContent(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
           throw new RuntimeException("Can not render file " + path + ". File does not exist");
        }
    }

}
