package com.fcgl;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Template for connecting our database
 */
public class DatabaseConnection {

    //TODO: This has to return a connection so that someone can access the DB.
    //For now this is a template for how to connect to a database. Can do DB logic within the try{}
    public void connect() {
        String userName = "fcgl-user";//put this in a config
        String password = "password";//put this in a config
        //String useSSl = false;

        String url = "jdbc:mysql://localhost:3306/fcgl_local?useSSL=false&serverTimezone=UTC";//config
        // Connection is the only JDBC resource that we need
        // PreparedStatement and ReseultSet are handled by jOOQ, internally
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            System.out.println("Connected to Database");
        } catch(Exception e) {
            System.out.println("Could not connect to Database");
            e.printStackTrace();
        }
    }

}
