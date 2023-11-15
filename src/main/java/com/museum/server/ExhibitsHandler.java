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
        this.conn = Database.connect();
        List<Exhibit> exhibitsList = new ArrayList<>();

        try {
            this.stmt = conn.prepareStatement(query);
            this.result = stmt.executeQuery();

            while (this.result.next()) {
                Exhibit exhibit = new Exhibit(
                        result.getInt("exhibitID"),
                        result.getString("name"),
                        result.getString("author"),
                        result.getDate("creationDate"),
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

//        System.out.println("server");
//        for (Exhibit ex : exhibitsList) {
//            System.out.println(ex.exhibitID + " " + ex.name + " " + ex.status);
//        }

        return exhibitsList;
    }

}
