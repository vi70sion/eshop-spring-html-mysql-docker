package com.example.eshop;

public class Constants {
    public final static String URL = "jdbc:mysql://127.0.0.1:3308/eshop";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "1234";

    private Constants() {
        throw new AssertionError("Cannot instantiate the Constants class");
    }

}
