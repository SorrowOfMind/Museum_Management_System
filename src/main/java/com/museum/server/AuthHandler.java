package com.museum.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthHandler {
    private String username;
    private String password;

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;

    public AuthHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // the auth method is only a template, users shouldn't authorized like this!!
    // TODO: when registration added to admin user, all passwords will be encrypted (eg. with BCrypt), and hashed passwords will be compared during auth!
    // TODO: make a joined query to get more user data
    public boolean authenticate() {
        String query = "SELECT * FROM users WHERE username = ? and password = ?";
        this.conn = Database.connect();

        try {
            this.stmt = conn.prepareStatement(query);
            this.stmt.setString(1, this.username);
            this.stmt.setString(2, this.password);
            this.result = stmt.executeQuery();

            if (this.result.next()) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
