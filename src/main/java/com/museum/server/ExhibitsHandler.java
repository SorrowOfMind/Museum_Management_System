package com.museum.server;

import com.museum.models.Exhibit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExhibitsHandler {
    public final String  IMAGES_DIR = "Images";
    public final String IMAGE_FILE_NAME = "exhibit_";
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;
    private String dbFilePath = null;
    private String filePath = null;

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
                        result.getString("security"),
                        result.getString("filePath")
                );
                exhibitsList.add(exhibit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exhibitsList;
    }

    public List<Exhibit> addExhibit(Exhibit exhibit, byte[] imageData) {
        String query = "INSERT INTO exhibit (exhibitID, name, author, creationDate, origins, description, acquisitionDate, value, ageID, lastConservation, nextConservation, status,security, filePath)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String lastIDQuery = "SELECT MAX(exhibitID) as exhibitID FROM exhibit";
        int exhibitID;

        conn = Database.connect();

        try {
            stmt = conn.prepareStatement(lastIDQuery);
            result = stmt.executeQuery();

            if (result.next()) {
                exhibitID = result.getInt("exhibitID") + 1;
                if (imageData != null) {
                    String fileName = "exhibit_" + exhibitID;
                    filePath = createFilePath(fileName);
                    dbFilePath = createDbFilePath(fileName);
                    saveImage(imageData, filePath);
                }

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
                stmt.setString(14, dbFilePath);

                stmt.executeUpdate();
            }

            return getExhibits();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Exhibit> updateExhibit(Exhibit exhibit, byte[] imageData) {
        String exhibitIDQuery = "SELECT exhibitID FROM exhibit WHERE exhibitID = ?";
        String updateQuery = "UPDATE exhibit SET name = ?, author = ?, creationDate = ?, origins = ?, description = ?, acquisitionDate = ?, value = ?, ageID = ?, lastConservation = ?, nextConservation =? , status = ?, security = ?, filePath = ? " +
                "WHERE exhibitID = ?";
        int exhibitID = exhibit.getExhibitID();
        System.out.println("id " + exhibit.getExhibitID());
        conn = Database.connect();

        try {
            stmt = conn.prepareStatement(exhibitIDQuery);
            stmt.setInt(1, exhibitID);
            result = stmt.executeQuery();

            if (imageData != null) {
                String fileName = createFileName(exhibitID);
                filePath = createFilePath(fileName);
                dbFilePath = createDbFilePath(fileName);
                saveImage(imageData, filePath);
            }

            if (result.next()) {
                stmt = conn.prepareStatement(updateQuery);
                stmt.setString(1, exhibit.getName());
                stmt.setString(2, exhibit.getAuthor());
                stmt.setString(3, exhibit.getCreationDate());
                stmt.setString(4, exhibit.getOrigins());
                stmt.setString(5, exhibit.getDescription());
                stmt.setDate(6, exhibit.getAcquisitionDate());
                stmt.setInt(7, exhibit.getValue());
                stmt.setInt(8, exhibit.getAgeID());
                stmt.setDate(9, exhibit.getLastConservation());
                stmt.setDate(10, exhibit.getNextConservation());
                stmt.setString(11, exhibit.getStatus());
                stmt.setString(12, exhibit.getSecurity());
                stmt.setString(13, dbFilePath);
                stmt.setInt(14, exhibitID);

                stmt.executeUpdate();
            }

            return getExhibits();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveImage(byte[] imageData, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageData);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createFilePath(String fileName) {
        return IMAGES_DIR + "/" + fileName + ".png";
    }

    private String createDbFilePath(String fileName) {
        return "\\" + IMAGES_DIR + "\\" + fileName + ".png";
    }

    private String createFileName(int exhibitID) {
        return IMAGE_FILE_NAME + exhibitID;
    }

}
