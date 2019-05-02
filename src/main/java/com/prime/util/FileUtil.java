package com.prime.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    /**
     * Creates a File if it don't exist
     *
     * @param path the path to the  File
     * @return the generated File or if it exist the old one.
     */
    public static File createFileIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
