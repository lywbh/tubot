package com.kamimi.tubot.module;

import lombok.SneakyThrows;

import java.io.*;

public class PetpetModule {

    private static final String pythonPath = "E:\\python3\\python.exe";
    private static final String scriptPath = "E:\\mirai\\plugins\\petpet.py";

    @SneakyThrows
    public static String petpet(long qq) {
        String[] args = new String[]{pythonPath, scriptPath, String.valueOf(qq)};
        Process proc = Runtime.getRuntime().exec(args);
        proc.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line, last = null;
        while ((line = reader.readLine()) != null) {
            last = line;
        }
        return last;
    }

}
