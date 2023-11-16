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

    public void addExhibit(Exhibit exhibit) {
        String query = "INSERT INTO exhibit (name, author, creationDate, origins, description, acquisitionDate, value, ageID, lastConservation, nextConservation, status,security)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conn = Database.connect();

        try {
            // TODO: zrobic ten cholerny AI na exhibitID
            stmt = conn.prepareStatement(query);
            stmt.setString(1, exhibit.getName());
            stmt.setString(2, exhibit.getAuthor());
            stmt.setString(3, exhibit.getCreationDate());
            stmt.setString(4, exhibit.getOrigins());
            stmt.setString(5, exhibit.getDescription());
            stmt.setString(6, String.valueOf(exhibit.getAcquisitionDate()));
            stmt.setString(7, String.valueOf(exhibit.getValue()));
            stmt.setString(8, String.valueOf(exhibit.getAgeID()));
            stmt.setString(9, String.valueOf(exhibit.getLastConservation()));
            stmt.setString(10, String.valueOf(exhibit.getNextConservation()));
            stmt.setString(11, exhibit.getStatus());
            stmt.setString(12, exhibit.getSecurity());

            stmt.executeUpdate();

            System.out.println("added! :D");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
