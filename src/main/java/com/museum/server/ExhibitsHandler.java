package com.museum.server;

import com.museum.models.Exhibit;

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
        String query = "SELECT * FROM exhibits";
        this.conn = Database.connect();
        List<Exhibit> exhibitsList = new ArrayList<>();

        try {
            this.stmt = conn.prepareStatement(query);
            this.result = stmt.executeQuery();

            while (this.result.next()) {
                Exhibit exhibit = new Exhibit(result.getInt("id"), result.getString("name"), result.getString("status"));
                exhibitsList.add(exhibit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("server");
        for (Exhibit ex : exhibitsList) {
            System.out.println(ex.id + " " + ex.name + " " + ex.status);
        }

        return exhibitsList;
    }
}
