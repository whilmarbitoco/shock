package org.whilmarbitoco.core.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class File {

    public static String loadContent(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
           throw new RuntimeException(path + " file not found");
        }
    }

}
