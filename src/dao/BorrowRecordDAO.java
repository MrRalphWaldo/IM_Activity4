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

    /**
     * Create a borrow record using the stored procedure
     * @return generated borrow ID, or -1 if failed
     */
    public int createBorrowWithProcedure(int borrowerId, int custodianId, String borrowType,
                                         Integer classId, Integer eventId, String expectedReturnDate,
                                         String notes, int equipmentId, int quantity,
                                         String conditionOnCheckout) {
        String call = "{call sp_checkout_equipment(?,?,?,?,?,?,?,?,?,?,?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(call)) {

            cstmt.setInt(1, borrowerId);
            cstmt.setInt(2, custodianId);
            cstmt.setString(3, borrowType);
            if (classId == null) {
                cstmt.setNull(4, Types.INTEGER);
            } else {
                cstmt.setInt(4, classId);
            }
            if (eventId == null) {
                cstmt.setNull(5, Types.INTEGER);
            } else {
                cstmt.setInt(5, eventId);
            }
            cstmt.setDate(6, Date.valueOf(expectedReturnDate));
            cstmt.setString(7, notes);
            cstmt.setInt(8, equipmentId);
            cstmt.setInt(9, quantity);
            cstmt.setString(10, conditionOnCheckout);
            cstmt.registerOutParameter(11, Types.INTEGER);

            cstmt.execute();
            return cstmt.getInt(11);
        } catch (SQLException e) {
            System.err.println("Error creating borrow record: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Check if a class exists
     */
    public boolean classExists(int classId) {
        String query = "SELECT 1 FROM LABORATORY_CLASSES WHERE class_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking class existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Check if an event exists
     */
    public boolean eventExists(int eventId) {
        String query = "SELECT 1 FROM EVENTS WHERE event_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking event existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Check if a borrow record exists
     */
    public boolean borrowRecordExists(int borrowId) {
        String query = "SELECT 1 FROM BORROW_RECORDS WHERE borrow_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, borrowId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking borrow record existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get current return status for a borrow record
     */
    public String getBorrowRecordStatus(int borrowId) {
        String query = "SELECT return_status FROM BORROW_RECORDS WHERE borrow_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, borrowId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("return_status");
            }
        } catch (SQLException e) {
            System.err.println("Error checking borrow record status: " + e.getMessage());
        }
        return null;
    }

    /**
     * Check if a borrow detail exists for a given borrow and equipment
     */
    public boolean borrowDetailExists(int borrowId, int equipmentId) {
        String query = "SELECT 1 FROM BORROW_DETAILS WHERE borrow_id = ? AND equipment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, borrowId);
            pstmt.setInt(2, equipmentId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking borrow detail existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update condition on return for a borrowed item
     */
    public boolean updateBorrowDetailReturn(int borrowId, int equipmentId, String conditionOnReturn, String damageNotes) {
        String query = "UPDATE BORROW_DETAILS SET condition_on_return = ?, damage_notes = ? " +
                       "WHERE borrow_id = ? AND equipment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, conditionOnReturn);
            pstmt.setString(2, damageNotes);
            pstmt.setInt(3, borrowId);
            pstmt.setInt(4, equipmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating borrow detail return: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mark a borrow record as returned
     */
    public boolean markBorrowRecordReturned(int borrowId) {
        String query = "UPDATE BORROW_RECORDS SET actual_return_date = NOW(), return_status = 'RETURNED' " +
                       "WHERE borrow_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, borrowId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating borrow record return status: " + e.getMessage());
        }
        return false;
    }
}
