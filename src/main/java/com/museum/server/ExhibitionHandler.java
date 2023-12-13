package com.museum.server;

import com.museum.models.Exhibition;
import com.museum.models.Room;
import com.museum.models.Worker_Basic;

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


    public Integer insertUpdateExhibition(Exhibition ex, boolean update) {
        String insertUpdateQuery = "INSERT INTO exhibition(title, startDate, endDate) VALUES (?, ?, ?)";
        if(update){
            insertUpdateQuery = "UPDATE exhibition SET title = ?, startDate = ?, endDate = ? WHERE exhibitionID = ?";
        }

        String insertUpdateExhibitsForExhibition = "INSERT INTO exhibit_exhibition(exhibitionID, exhibitID) VALUES (?, ?) ON DUPLICATE KEY UPDATE exhibitionID = VALUES(exhibitionID), exhibitID = VALUES(exhibitID)";
        String insertUpdateExhibitionRoom = "INSERT INTO exhibition_room(exhibitionID, roomID) VALUES (?, ?) ON DUPLICATE KEY UPDATE exhibitionID = VALUES(exhibitionID), roomID = VALUES(roomID)";
        String insertUpdateExhibitionWorker = "INSERT INTO worker_exhibition(exhibitionID, workerID) VALUES (?, ?) ON DUPLICATE KEY UPDATE exhibitionID = VALUES(exhibitionID), workerID = VALUES(workerID)";



        String selectLastIdQuery = "SELECT LAST_INSERT_ID() as id";

        this.conn = Database.connect();

        Integer id = ex.getExhibitionID();

        try {
            this.conn.setAutoCommit(false);

            this.stmt = conn.prepareStatement(insertUpdateQuery);
            this.stmt.setString(1, ex.getTitle());
            this.stmt.setDate(2, ex.getStartDate());
            this.stmt.setDate(3, ex.getEndDate());
            if(update)
                this.stmt.setInt(4, ex.getExhibitionID());

            this.stmt.executeUpdate();


            this.stmt = conn.prepareStatement(selectLastIdQuery);
            this.result = stmt.executeQuery();

            while (this.result.next()) {
                if(!update)
                id = result.getInt("id");
            }
            this.stmt = conn.prepareStatement(insertUpdateExhibitsForExhibition);
            this.stmt.setInt(1, id);

            for(Integer exhibitID : ex.getExhibitIds()){
                this.stmt.setInt(2,exhibitID);
                this.stmt.executeUpdate();

            }

            this.stmt = conn.prepareStatement(insertUpdateExhibitionRoom);
            this.stmt.setInt(1, id);

            for(Integer x : ex.getRoomIDs()){
                this.stmt.setInt(2,x);
                this.stmt.executeUpdate();

            }

            this.stmt = conn.prepareStatement(insertUpdateExhibitionWorker);
            this.stmt.setInt(1, id);

            for(Integer x : ex.getWorkerIDs()){
                this.stmt.setInt(2,x);
                this.stmt.executeUpdate();

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
                 if(this.conn !=null){
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

//    public List<Exhibition> getExhibitions(String filter) {
//        String query = "SELECT * FROM exhibition natural join exhibit_exhibition natural join worker_exhibition natural join room ";
//        if(filter != null || !filter.isEmpty())
//            query += " WHERE exhibition.title LIKE ?" ;
//        query += " ORDER BY exhibitionID";
//        this.conn = Database.connect();
//        List<Exhibition> exhibitionList = new ArrayList<>();
//        Integer prev = null;
//
//        try {
//            this.stmt = conn.prepareStatement(query);
//            if(filter != null || !filter.isEmpty())
//                this.stmt.setString(1, filter+"%");
//            this.result = stmt.executeQuery();
//            Exhibition exhibition = null;
//            while (this.result.next()) {
//                final int id = result.getInt("exhibitionID");
//                if(prev == null || prev != id){
//                    prev = id;
//                    exhibition = new Exhibition(id,
//                            result.getString("title"),
//                            result.getDate("startDate"),
//                            result.getDate("endDate")
//                    );
//
//                    exhibition.appendIDs( result.getInt("exhibitID"),result.getInt("roomID"), result.getInt("workerID") );
//                    exhibitionList.add(exhibition);
//                }
//                else if(id == prev && exhibition != null){
//                    exhibition.appendIDs( result.getInt("exhibitID"), result.getInt("roomID"),  result.getInt("workerID"));
//                }
//
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return exhibitionList;
//    }

    public List<Exhibition> getExhibitions() {
        String query = "SELECT * FROM exhibition natural join exhibit_exhibition natural join worker_exhibition natural join room ";
        query += " ORDER BY exhibitionID";
        this.conn = Database.connect();
        List<Exhibition> exhibitionList = new ArrayList<>();
        Integer prev = null;

        try {
            this.stmt = conn.prepareStatement(query);
            this.result = stmt.executeQuery();
            Exhibition exhibition = null;
            while (this.result.next()) {
                final int id = result.getInt("exhibitionID");
                if(prev == null || prev != id){
                    prev = id;
                    exhibition = new Exhibition(id,
                            result.getString("title"),
                            result.getDate("startDate"),
                            result.getDate("endDate")
                    );

                    exhibition.appendIDs( result.getInt("exhibitID"),result.getInt("roomID"), result.getInt("workerID") );
                    exhibitionList.add(exhibition);
                }
                else if(id == prev && exhibition != null){
                    exhibition.appendIDs( result.getInt("exhibitID"), result.getInt("roomID"),  result.getInt("workerID"));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exhibitionList;
    }

    public List<Room> getRooms(){
        String query = "SELECT * FROM room";
        this.conn = Database.connect();
        List<Room> roomList = new ArrayList<>();

        try {
            this.stmt = conn.prepareStatement(query);
            this.result = stmt.executeQuery();

            while (this.result.next()) {
                Room room = new Room(result.getInt("roomID"),
                        result.getInt("roomNumber"),
                        result.getInt("floor"),
                        result.getInt("area")
                );
                roomList.add(room);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return roomList;
    }

    public List<Worker_Basic> getWorkersForExhibition(){
        String query = "SELECT workerID, forename, surname FROM worker";
        this.conn = Database.connect();
        List<Worker_Basic> workerList = new ArrayList<>();

        try {
            this.stmt = conn.prepareStatement(query);
            this.result = stmt.executeQuery();

            while (this.result.next()) {
                Worker_Basic worker = new Worker_Basic(
                        result.getInt("workerID"),
                        result.getString("forename"),
                        result.getString("surname")

                );
                workerList.add(worker);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return workerList;
    }

}
