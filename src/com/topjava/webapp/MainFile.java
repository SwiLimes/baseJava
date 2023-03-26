package com.topjava.webapp;

import java.io.File;

public class MainFile {

    public static String repeat(String str, int repeatNum) {
        return new String(new char[repeatNum]).replaceAll("\0", str);
    }
    public static void findFiles(File dir, int deepLvl) {
        String indent = repeat("   ", deepLvl);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    System.out.println(indent + "File:" + f.getName());
                } else {
                    System.out.println(indent + "Directory: " + f.getName());
                    findFiles(f, deepLvl + 1);
                }
            }
        }
    }
    public static void main(String[] args) {
        String pathFile = "/Users/nikitaberdennikov/IdeaProjects/baseJava/src";
        File file = new File(pathFile);
        findFiles(file, 0);
    }
}
