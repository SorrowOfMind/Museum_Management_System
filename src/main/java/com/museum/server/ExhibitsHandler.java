package com.museum.server;

import com.museum.models.Exhibit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExhibitsHandler {
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;

    public List<Exhibit> getExhibits() {
        String query = "SELECT * FROM exhibit";
        conn = Database.connect();
        List<Exhibit> exhibitsList = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(query);
            result = stmt.executeQuery();

            while (this.result.next()) {
                Exhibit exhibit = new Exhibit(
                        result.getInt("exhibitID"),
                        result.getString("name"),
                        result.getString("author"),
                        result.getString("creationDate"),
                        result.getString("origins"),
                        result.getString("description"),
                        result.getDate("acquisitionDate"),
                        result.getInt("value"),
                        result.getInt("ageID"),
                        result.getDate("lastConservation"),
                        result.getDate("nextConservation"),
                        result.getString("status"),
                        result.getString("security")
                );
                exhibitsList.add(exhibit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exhibitsList;
    }

    public List<Exhibit> addExhibit(Exhibit exhibit) {
        String query = "INSERT INTO exhibit (exhibitID, name, author, creationDate, origins, description, acquisitionDate, value, ageID, lastConservation, nextConservation, status,security)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String lastIDQuery = "SELECT MAX(exhibitID) as exhibitID FROM exhibit";
        int exhibitID;

        conn = Database.connect();

        try {
            stmt = conn.prepareStatement(lastIDQuery);
            result = stmt.executeQuery();

            if (result.next()) {
                exhibitID = result.getInt("exhibitID") + 1;
                System.out.println("exhibitID " + exhibitID + " " +  exhibit.getAgeID());
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, exhibitID);
                stmt.setString(2, exhibit.getName());
                stmt.setString(3, exhibit.getAuthor());
                stmt.setString(4, exhibit.getCreationDate());
                stmt.setString(5, exhibit.getOrigins());
                stmt.setString(6, exhibit.getDescription());
                stmt.setDate(7, exhibit.getAcquisitionDate());
                stmt.setInt(8, exhibit.getValue());
                stmt.setInt(9, exhibit.getAgeID());
                stmt.setDate(10, exhibit.getLastConservation());
                stmt.setDate(11, exhibit.getNextConservation());
                stmt.setString(12, exhibit.getStatus());
                stmt.setString(13, exhibit.getSecurity());

                stmt.executeUpdate();
            }

            return getExhibits();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
