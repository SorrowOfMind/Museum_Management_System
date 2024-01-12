package com.museum.server;

import com.museum.models.Tour;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourHandler {
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;

    public List<Tour> insertUpdateTour(Tour tour, boolean update) {
        String insertUpdateQuery = "INSERT INTO tour(groupLeader, tourDate, tourHour, size, language, standardTicketCount, discountTicketCount, workerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        if (update) {
            insertUpdateQuery = "UPDATE tour SET groupLeader = ?, tourDate = ?, tourHour = ?, size = ?, language = ?, standardTicketCount = ?, discountTicketCount = ?, workerID = ? WHERE tourID = ?";
        }

        this.conn = Database.connect();

        Integer id = tour.getTourID();

        try {
            this.conn.setAutoCommit(false);

            this.stmt = conn.prepareStatement(insertUpdateQuery);
            this.stmt.setString(1, tour.getGroupLeader());
            this.stmt.setDate(2, Date.valueOf(tour.getTourDate()));
            this.stmt.setString(3, tour.getTourHour());
            this.stmt.setInt(4, tour.getSize());
            this.stmt.setString(5, tour.getLanguage());
            this.stmt.setInt(6, tour.getStandardTicketCount());
            this.stmt.setInt(7, tour.getDiscountTicketCount());
            this.stmt.setInt(8, tour.getWorkerID());

            if (update) {
                this.stmt.setInt(9, tour.getTourID());
            }

            this.stmt.executeUpdate();

            this.conn.commit();
            return getTours();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Tour> getTours() {
        String query = "SELECT * FROM tour ";

        this.conn = Database.connect();
        List<Tour> tourList = new ArrayList<>();
        try {
            this.stmt = conn.prepareStatement(query);
            this.result = stmt.executeQuery();
            while (this.result.next()) {
                    Tour tour = new Tour(result.getInt("tourID"),
                            result.getString("groupLeader"),
                            result.getDate("tourDate").toLocalDate(),
                            result.getString("tourHour"),
                            result.getInt("size"),
                            result.getString("language"),
                            result.getInt("workerID"),
                            result.getInt("standardTicketCount"),
                            result.getInt("discountTicketCount")

                    );
                    tourList.add(tour);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources();
        }

        return tourList;
    }

    private void closeResources() {
        try {
            if (this.conn != null) {
                this.conn.close();
            }
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

}
