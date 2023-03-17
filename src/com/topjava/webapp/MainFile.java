package com.topjava.webapp;

import java.io.File;

public class MainFile {
    public static void findFiles(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    System.out.println(f.getName());
                } else {
                    findFiles(f);
                }
            }
        }
    }
    public static void main(String[] args) {
        String pathFile = ".";
        File file = new File(pathFile);
        findFiles(file);
    }
}
