package com.museum.server;

import com.museum.models.Visitor;
import com.museum.models.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;
    public List<Visitor> getVisitorsData() {
        String query = "SELECT * FROM visitor ORDER BY ID DESC LIMIT 12";
        conn = Database.connect();
        List<Visitor> visitorsData = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(query);
            result = stmt.executeQuery();
            while (result.next()) {
                Visitor visitorData = new Visitor(
                        result.getString("month"),
                        result.getInt("number")
                );
                visitorsData.add(visitorData);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return visitorsData;
    }

}
