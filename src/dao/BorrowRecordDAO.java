package dao;

import models.BorrowRecord;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BorrowRecord DAO - Data Access Object for Borrow Record operations
 * Implements READ operations for retrieving borrow information
 */
public class BorrowRecordDAO {

    /**
     * Get all borrow records
     * @return List of all BorrowRecord objects
     */
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String query = "SELECT br.borrow_id, CONCAT(u1.first_name, ' ', u1.last_name) as borrower_name, " +
                       "CONCAT(u2.first_name, ' ', u2.last_name) as custodian_name, " +
                       "COALESCE(lc.class_name, e.event_name) as activity_name, " +
                       "br.borrow_type, br.borrow_date, br.expected_return_date, br.return_status " +
                       "FROM BORROW_RECORDS br " +
                       "JOIN USERS u1 ON br.borrower_id = u1.user_id " +
                       "JOIN USERS u2 ON br.custodian_id = u2.user_id " +
                       "LEFT JOIN LABORATORY_CLASSES lc ON br.class_id = lc.class_id " +
                       "LEFT JOIN EVENTS e ON br.event_id = e.event_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getInt("borrow_id"),
                    rs.getString("borrower_name"),
                    rs.getString("custodian_name"),
                    rs.getString("activity_name"),
                    rs.getString("borrow_type"),
                    rs.getTimestamp("borrow_date").toLocalDateTime(),
                    rs.getString("expected_return_date"),
                    rs.getString("return_status")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all borrow records: " + e.getMessage());
        }
        return records;
    }

    /**
     * Get borrow records by status
     * @param status The return status (ACTIVE, RETURNED, OVERDUE)
     * @return List of BorrowRecord objects with the specified status
     */
    public List<BorrowRecord> getBorrowRecordsByStatus(String status) {
        List<BorrowRecord> records = new ArrayList<>();
        String query = "SELECT br.borrow_id, CONCAT(u1.first_name, ' ', u1.last_name) as borrower_name, " +
                       "CONCAT(u2.first_name, ' ', u2.last_name) as custodian_name, " +
                       "COALESCE(lc.class_name, e.event_name) as activity_name, " +
                       "br.borrow_type, br.borrow_date, br.expected_return_date, br.return_status " +
                       "FROM BORROW_RECORDS br " +
                       "JOIN USERS u1 ON br.borrower_id = u1.user_id " +
                       "JOIN USERS u2 ON br.custodian_id = u2.user_id " +
                       "LEFT JOIN LABORATORY_CLASSES lc ON br.class_id = lc.class_id " +
                       "LEFT JOIN EVENTS e ON br.event_id = e.event_id " +
                       "WHERE br.return_status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getInt("borrow_id"),
                    rs.getString("borrower_name"),
                    rs.getString("custodian_name"),
                    rs.getString("activity_name"),
                    rs.getString("borrow_type"),
                    rs.getTimestamp("borrow_date").toLocalDateTime(),
                    rs.getString("expected_return_date"),
                    rs.getString("return_status")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving borrow records by status: " + e.getMessage());
        }
        return records;
    }

    /**
     * Get borrow records by borrower ID
     * @param borrowerId The borrower user ID
     * @return List of BorrowRecord objects for the specified borrower
     */
    public List<BorrowRecord> getBorrowRecordsByBorrower(int borrowerId) {
        List<BorrowRecord> records = new ArrayList<>();
        String query = "SELECT br.borrow_id, CONCAT(u1.first_name, ' ', u1.last_name) as borrower_name, " +
                       "CONCAT(u2.first_name, ' ', u2.last_name) as custodian_name, " +
                       "COALESCE(lc.class_name, e.event_name) as activity_name, " +
                       "br.borrow_type, br.borrow_date, br.expected_return_date, br.return_status " +
                       "FROM BORROW_RECORDS br " +
                       "JOIN USERS u1 ON br.borrower_id = u1.user_id " +
                       "JOIN USERS u2 ON br.custodian_id = u2.user_id " +
                       "LEFT JOIN LABORATORY_CLASSES lc ON br.class_id = lc.class_id " +
                       "LEFT JOIN EVENTS e ON br.event_id = e.event_id " +
                       "WHERE br.borrower_id = ? " +
                       "ORDER BY br.borrow_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, borrowerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getInt("borrow_id"),
                    rs.getString("borrower_name"),
                    rs.getString("custodian_name"),
                    rs.getString("activity_name"),
                    rs.getString("borrow_type"),
                    rs.getTimestamp("borrow_date").toLocalDateTime(),
                    rs.getString("expected_return_date"),
                    rs.getString("return_status")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving borrow records by borrower: " + e.getMessage());
        }
        return records;
    }

    /**
     * Get borrow records by classroom
     * @param classId The class ID
     * @return List of BorrowRecord objects for the specified class
     */
    public List<BorrowRecord> getBorrowRecordsByClass(int classId) {
        List<BorrowRecord> records = new ArrayList<>();
        String query = "SELECT br.borrow_id, CONCAT(u1.first_name, ' ', u1.last_name) as borrower_name, " +
                       "CONCAT(u2.first_name, ' ', u2.last_name) as custodian_name, " +
                       "lc.class_name as activity_name, " +
                       "br.borrow_type, br.borrow_date, br.expected_return_date, br.return_status " +
                       "FROM BORROW_RECORDS br " +
                       "JOIN USERS u1 ON br.borrower_id = u1.user_id " +
                       "JOIN USERS u2 ON br.custodian_id = u2.user_id " +
                       "JOIN LABORATORY_CLASSES lc ON br.class_id = lc.class_id " +
                       "WHERE br.class_id = ? " +
                       "ORDER BY br.borrow_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getInt("borrow_id"),
                    rs.getString("borrower_name"),
                    rs.getString("custodian_name"),
                    rs.getString("activity_name"),
                    rs.getString("borrow_type"),
                    rs.getTimestamp("borrow_date").toLocalDateTime(),
                    rs.getString("expected_return_date"),
                    rs.getString("return_status")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving borrow records by class: " + e.getMessage());
        }
        return records;
    }

    /**
     * Get unreturned borrow records
     * @return List of active/unreturned BorrowRecord objects
     */
    public List<BorrowRecord> getUnreturnedRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String query = "SELECT br.borrow_id, CONCAT(u1.first_name, ' ', u1.last_name) as borrower_name, " +
                       "CONCAT(u2.first_name, ' ', u2.last_name) as custodian_name, " +
                       "COALESCE(lc.class_name, e.event_name) as activity_name, " +
                       "br.borrow_type, br.borrow_date, br.expected_return_date, br.return_status " +
                       "FROM BORROW_RECORDS br " +
                       "JOIN USERS u1 ON br.borrower_id = u1.user_id " +
                       "JOIN USERS u2 ON br.custodian_id = u2.user_id " +
                       "LEFT JOIN LABORATORY_CLASSES lc ON br.class_id = lc.class_id " +
                       "LEFT JOIN EVENTS e ON br.event_id = e.event_id " +
                       "WHERE br.return_status IN ('ACTIVE', 'OVERDUE') " +
                       "ORDER BY br.expected_return_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getInt("borrow_id"),
                    rs.getString("borrower_name"),
                    rs.getString("custodian_name"),
                    rs.getString("activity_name"),
                    rs.getString("borrow_type"),
                    rs.getTimestamp("borrow_date").toLocalDateTime(),
                    rs.getString("expected_return_date"),
                    rs.getString("return_status")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving unreturned records: " + e.getMessage());
        }
        return records;
    }

    /**
     * Get a specific borrow record by ID
     * @param borrowId The borrow record ID
     * @return BorrowRecord object if found, null otherwise
     */
    public BorrowRecord getBorrowRecordById(int borrowId) {
        String query = "SELECT br.borrow_id, CONCAT(u1.first_name, ' ', u1.last_name) as borrower_name, " +
                       "CONCAT(u2.first_name, ' ', u2.last_name) as custodian_name, " +
                       "COALESCE(lc.class_name, e.event_name) as activity_name, " +
                       "br.borrow_type, br.borrow_date, br.expected_return_date, br.actual_return_date, " +
                       "br.return_status, br.notes " +
                       "FROM BORROW_RECORDS br " +
                       "JOIN USERS u1 ON br.borrower_id = u1.user_id " +
                       "JOIN USERS u2 ON br.custodian_id = u2.user_id " +
                       "LEFT JOIN LABORATORY_CLASSES lc ON br.class_id = lc.class_id " +
                       "LEFT JOIN EVENTS e ON br.event_id = e.event_id " +
                       "WHERE br.borrow_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, borrowId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getInt("borrow_id"),
                    rs.getString("borrower_name"),
                    rs.getString("custodian_name"),
                    rs.getString("activity_name"),
                    rs.getString("borrow_type"),
                    rs.getTimestamp("borrow_date").toLocalDateTime(),
                    rs.getString("expected_return_date"),
                    rs.getString("return_status")
                );
                record.setNotes(rs.getString("notes"));
                return record;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving borrow record by ID: " + e.getMessage());
        }
        return null;
    }
}

