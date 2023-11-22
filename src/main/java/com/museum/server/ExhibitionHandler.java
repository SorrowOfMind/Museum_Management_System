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


    public Integer insertExhibition(Exhibition ex) {
        String insertQuery = "INSERT INTO exhibition(title, startDate, endDate) VALUES (?, ?, ?)";
        String selectLastIdQuery = "SELECT LAST_INSERT_ID() as id";

        String insertExhibitsForExhibition = "INSERT INTO exhibit_exhibition(exhibitionID, exhibitID) values (?, ?)";
        String insertExhibitionRoom = "INSERT INTO exhibition_room(exhibitionID, roomID) values (?, ?)";

        String insertExhibitionWorker = "INSERT INTO worker_exhibition(exhibitionID, workerID) values (?, ?)";


        this.conn = Database.connect();

        Integer id = -1;

        try {
            this.conn.setAutoCommit(false);

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

            this.stmt = conn.prepareStatement(insertExhibitionRoom);
            this.stmt.setInt(1, id);

            for(Integer x : ex.getRoomIDs()){
                this.stmt.setInt(2,x);
                this.stmt.executeUpdate();

            }

            this.stmt = conn.prepareStatement(insertExhibitionWorker);
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

    public List<Exhibition> getExhibitions(String filter) {
        String query = "SELECT * FROM exhibition natural join exhibit_exhibition natural join worker_exhibition natural join room ";
        if(filter != null || !filter.isEmpty())
            query += " WHERE exhibition.title LIKE ?" ;
        query += " ORDER BY exhibitionID";
        this.conn = Database.connect();
        List<Exhibition> exhibitionList = new ArrayList<>();
        Integer prev = null;

        try {
            this.stmt = conn.prepareStatement(query);
            if(filter != null || !filter.isEmpty())
                this.stmt.setString(1, filter+"%");
            this.result = stmt.executeQuery();
            Exhibition exhibition = null;
            while (this.result.next()) {
                final int id = result.getInt("exhibitionID");
                System.out.println("ID" + (id));
                if(prev == null || prev != id){
                    System.out.println("first if");
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
                    System.out.println("second if");
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
