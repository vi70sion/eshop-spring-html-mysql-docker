package com.example.eshop;

public class Constants {
    public final static String URL = "jdbc:mysql://localhost:3306/eshop";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "Mysql23*";

    private Constants() {
        throw new AssertionError("Cannot instantiate the Constants class");
    }

}
