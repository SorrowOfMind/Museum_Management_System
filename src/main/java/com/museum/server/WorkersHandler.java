package com.museum.server;


import com.museum.models.Worker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkersHandler {
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;

    public List<Worker> getWorkers() {
        String query = "SELECT * FROM worker";
        conn = Database.connect();
        List<Worker> workersList = new ArrayList<>();

        try {
            stmt = conn.prepareStatement(query);
            result = stmt.executeQuery();

            while (this.result.next()) {
                Worker worker = new Worker(
                        result.getInt("workerID"),
                        result.getString("forename"),
                        result.getString("surname"),
                        result.getDate("dateOfBirth"),
                        result.getString("phoneNumber"),
                        result.getString("email"),
                        result.getDate("dateOfTermination"),
                        result.getString("agreementType"),
                        result.getDate("dateOfAgreement"),
                        result.getString("accountNumber"),
                        result.getInt("salary"),
                        result.getString("jobTitle")
                );
                workersList.add(worker);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return workersList;
    }

    public List<Worker> addWorker(Worker worker) {
        String query = "INSERT INTO worker (forename, surname, dateOfBirth, phoneNumber, email, dateOfTermination, agreementType, dateOfAgreement, accountNumber, salary, jobTitle)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        conn = Database.connect();

        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, worker.getName());
            stmt.setString(2, worker.getSurname());
            stmt.setDate(3, worker.getDateOfBirth());
            stmt.setString(4, worker.getPhone());
            stmt.setString(5, worker.getEmail());
            stmt.setDate(6, worker.getTerminationDate());
            stmt.setString(7, worker.getAgreementType());
            stmt.setDate(8, worker.getAgreementDate());
            stmt.setString(9, worker.getAccountNumber());
            stmt.setInt(10, worker.getSalary());
            stmt.setString(11, worker.getJobTitle());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getWorkers();
    }

    public List<Worker> updateWorker(Worker worker) {

        String updateQuery = "UPDATE worker SET forename = ?, surname = ?, dateOfBirth = ?, phoneNumber = ?, email = ?, dateOfTermination = ?, agreementType = ?, dateOfAgreement = ?, accountNumber = ?, salary =? , jobTitle = ? " +
                "WHERE workerID = ?";
        int workerID = worker.getWorkerID();

        conn = Database.connect();

        try {
            stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, worker.getName());
            stmt.setString(2, worker.getSurname());
            stmt.setDate(3, worker.getDateOfBirth());
            stmt.setString(4, worker.getPhone());
            stmt.setString(5, worker.getEmail());
            stmt.setDate(6, worker.getTerminationDate());
            stmt.setString(7, worker.getAgreementType());
            stmt.setDate(8, worker.getAgreementDate());
            stmt.setString(9, worker.getAccountNumber());
            stmt.setInt(10, worker.getSalary());
            stmt.setString(11, worker.getJobTitle());
            stmt.setInt(12, workerID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("get workers");
        return getWorkers();
    }
}
