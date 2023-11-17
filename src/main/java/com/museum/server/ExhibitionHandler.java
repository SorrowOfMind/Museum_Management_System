package com.museum.server;

import com.museum.models.Exhibition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExhibitionHandler {
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;



    public Integer insertExhibition(Exhibition ex) {
        String insertQuery = "INSERT INTO exhibition(title, startDate, endDate) VALUES (?, ?, ?)";
        String selectLastIdQuery = "SELECT LAST_INSERT_ID() as id";

        String insertExhibitsForExhibition = "INSERT INTO exhibit_exhibition(exhibitionID, exhibitID) values (?, ?)";

        this.conn = Database.connect();
        Integer id = -1;

        try {
            // Insert the exhibition
            this.stmt = conn.prepareStatement(insertQuery);
            this.stmt.setString(1, ex.getTitle());
            this.stmt.setDate(2, ex.getStartDate());
            this.stmt.setDate(3, ex.getEndDate());
            this.stmt.executeUpdate();


            this.stmt = conn.prepareStatement(selectLastIdQuery);
            this.result = stmt.executeQuery();

            while (this.result.next()) {
                id = result.getInt("id");
            }

            this.stmt = conn.prepareStatement(insertExhibitsForExhibition);
            this.stmt.setInt(1, id);

            for(Integer exhibitID : ex.getExhibitIds()){
                this.stmt.setInt(2,exhibitID);
                this.stmt.executeUpdate();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
             try {
                if (stmt != null) {
                    stmt.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    public List<Exhibition> getExhibitions(String filter) {
        String query = "SELECT * FROM exhibition";
        if(filter != null || !filter.isEmpty())
            query += " WHERE exhibition.name LIKE ?" ;
        this.conn = Database.connect();
        List<Exhibition> exhibitsList = new ArrayList<>();

        try {
            this.stmt = conn.prepareStatement(query);
            this.stmt.setString(1, filter+"%");
            this.result = stmt.executeQuery();

            while (this.result.next()) {
                Exhibition exhibition = new Exhibition(result.getInt("exhibitionID"),
                        result.getString("name"),
                        result.getDate("startDate"),
                        result.getDate("endDate")
                );
                exhibitsList.add(exhibition);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exhibitsList;
    }

}
