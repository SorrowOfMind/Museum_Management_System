package com.museum.server;

import com.museum.models.Exhibition;
import com.museum.models.Room;
import com.museum.models.Tour;
import com.museum.models.Worker_Basic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourHandler {
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;


    public Integer insertUpdateTour(Tour tour, boolean update) {
        String insertUpdateQuery = "INSERT INTO tour(groupLeader, tourDate, tourHour, size, language, standardTicketCount, discountTicketCount, workerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        if (update) {
            insertUpdateQuery = "UPDATE tour SET groupLeader = ?, tourDate = ?, tourHour = ?, size = ?, language = ?, standardTicketCount = ?, discountTicketCount = ?, workerID = ? WHERE tourID = ?";
        }

        String selectLastIdQuery = "SELECT LAST_INSERT_ID() as id";

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
            System.out.println( this.stmt);

            this.stmt.executeUpdate();

            this.stmt = conn.prepareStatement(selectLastIdQuery);
            this.result = stmt.executeQuery();

            while (this.result.next()) {
                if (!update) {
                    id = result.getInt("id");
                }
            }

            this.conn.commit();

        } catch (SQLException e) {
            if (this.conn != null) {
                try {
                    this.conn.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace(); // Handle rollback failure
                }
            }
        } finally {
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

        return id;
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
