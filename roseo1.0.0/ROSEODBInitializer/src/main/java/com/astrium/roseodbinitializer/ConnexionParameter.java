/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.astrium.roseodbinitializer;

/**
 *
 * @author re-cetienne
 */
public class ConnexionParameter {
    private static String url;
    private static String user;
    private static String pass;
    private static String roseoDatabase;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ConnexionParameter.url = url;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        ConnexionParameter.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        ConnexionParameter.pass = pass;
    }

    public static String getRoseoDatabase() {
        return roseoDatabase;
    }

    public static void setRoseoDatabase(String roseoDatabase) {
        ConnexionParameter.roseoDatabase = roseoDatabase;
    }

}
