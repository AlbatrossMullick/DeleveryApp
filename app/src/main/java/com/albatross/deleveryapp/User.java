package com.albatross.deleveryapp;

public class User {
    private static String userName;
    private static String email;
    private static String uid;
    private static String userType;
    private static String Token;

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        User.uid = uid;
    }

    public static String getUserType() {
        return userType;
    }

    public static void setUserType(String userType) {
        User.userType = userType;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }


    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

}
