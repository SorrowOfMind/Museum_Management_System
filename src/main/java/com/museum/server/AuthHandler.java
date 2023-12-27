package com.museum.server;

import com.museum.models.Worker;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthHandler {
    private int role;
    private Worker user;

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;


    public boolean authenticate(String username, String password) {
        Encryptor encryptor = new Encryptor();
        String query = "SELECT * FROM user WHERE username = ? and password = ?";
        conn = Database.connect();

        try {
            String hashedPassword = encryptor.encrypt(password);
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            result = stmt.executeQuery();

            if (result.next()) {

                role = result.getInt("role");
                int userID = result.getInt("userID");
                String userQuery = "SELECT * FROM worker WHERE workerID = ?";
                try {
                    stmt = conn.prepareStatement(userQuery);
                    stmt.setInt(1, userID);
                    result = stmt.executeQuery();
                    if (result.next()) {
                        user = new Worker(
                                result.getInt("workerID"),
                                result.getString("forename"),
                                result.getString("surname"),
                                result.getDate("dateOfBirth"),
                                result.getString("phoneNumber"),
                                result.getString("email"),
                                result.getDate("dateOfTermination"),
                                result.getString("agreementType"),
                                result.getDate("dateOfAgreement"),
                                result.getString("accountNumber"),
                                result.getInt("salary"),
                                result.getString("jobTitle")
                        );
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean changePassword(int userID, String oldPassword, String newPassword) {
        Encryptor encryptor = new Encryptor();
        String query = "SELECT * FROM user WHERE userID = ? and password = ?";
        conn = Database.connect();

        try {
            String hashedOldPassword = encryptor.encrypt(oldPassword);
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userID);
            stmt.setString(2, hashedOldPassword);
            result = stmt.executeQuery();

            if (result.next()) {
                String updateQuery = "UPDATE user SET password = ? WHERE userID = ?";
                String hashedNewPassword = encryptor.encrypt(newPassword);
                stmt = conn.prepareStatement(updateQuery);
                stmt.setString(1, hashedNewPassword);
                stmt.setInt(2, userID);
                stmt.executeUpdate();

                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRole() {
        return role;
    }

    public Worker getUser() {
        return user;
    }
}

class Encryptor {
    public String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger bigInt = new BigInteger(1, messageDigest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
