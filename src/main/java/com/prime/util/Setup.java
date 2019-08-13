package com.prime.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Setup {

    private static final BufferedReader sys_in;

    static {
        InputStreamReader isr = new InputStreamReader(System.in);
        sys_in = new BufferedReader(isr);
    }

    public static String prompt(String req) {
        String token;

        // prompt for token
        System.out.println("Enter The Bots " + req + ":");


        try {
            // read and trim line
            String line = sys_in.readLine();
            token = line.trim();

            return token;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        // RAGEQUIT
        System.out.println("Exiting");
        Runtime.getRuntime().exit(1);
        return null;
    }
}
