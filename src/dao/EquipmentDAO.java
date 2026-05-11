package dao;

import models.Equipment;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Equipment DAO - Data Access Object for Equipment operations
 * Implements READ operations for retrieving equipment information
 */
public class EquipmentDAO {

    /**
     * Get all equipment
     * @return List of all Equipment objects
     */
    public List<Equipment> getAllEquipment() {
        List<Equipment> equipment = new ArrayList<>();
        String query = "SELECT e.equipment_id, e.barcode, e.equipment_name, c.category_name, " +
                       "e.status, e.location FROM EQUIPMENT e " +
                       "JOIN CATEGORIES c ON e.category_id = c.category_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Equipment eq = new Equipment(
                    rs.getInt("equipment_id"),
                    rs.getString("barcode"),
                    rs.getString("equipment_name"),
                    rs.getString("category_name"),
                    rs.getString("status"),
                    rs.getString("location")
                );
                equipment.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all equipment: " + e.getMessage());
        }
        return equipment;
    }

    /**
     * Get equipment by status
     * @param status The status to filter by (AVAILABLE, CHECKED_OUT, MAINTENANCE, RETIRED)
     * @return List of Equipment objects with the specified status
     */
    public List<Equipment> getEquipmentByStatus(String status) {
        List<Equipment> equipment = new ArrayList<>();
        String query = "SELECT e.equipment_id, e.barcode, e.equipment_name, c.category_name, " +
                       "e.status, e.location FROM EQUIPMENT e " +
                       "JOIN CATEGORIES c ON e.category_id = c.category_id " +
                       "WHERE e.status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Equipment eq = new Equipment(
                    rs.getInt("equipment_id"),
                    rs.getString("barcode"),
                    rs.getString("equipment_name"),
                    rs.getString("category_name"),
                    rs.getString("status"),
                    rs.getString("location")
                );
                equipment.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving equipment by status: " + e.getMessage());
        }
        return equipment;
    }

    /**
     * Get equipment by category
     * @param categoryId The category ID
     * @return List of Equipment objects in the specified category
     */
    public List<Equipment> getEquipmentByCategory(int categoryId) {
        List<Equipment> equipment = new ArrayList<>();
        String query = "SELECT e.equipment_id, e.barcode, e.equipment_name, c.category_name, " +
                       "e.status, e.location FROM EQUIPMENT e " +
                       "JOIN CATEGORIES c ON e.category_id = c.category_id " +
                       "WHERE e.category_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Equipment eq = new Equipment(
                    rs.getInt("equipment_id"),
                    rs.getString("barcode"),
                    rs.getString("equipment_name"),
                    rs.getString("category_name"),
                    rs.getString("status"),
                    rs.getString("location")
                );
                equipment.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving equipment by category: " + e.getMessage());
        }
        return equipment;
    }

    /**
     * Get a specific equipment by ID
     * @param equipmentId The equipment ID
     * @return Equipment object if found, null otherwise
     */
    public Equipment getEquipmentById(int equipmentId) {
        String query = "SELECT e.equipment_id, e.barcode, e.equipment_name, c.category_name, " +
                       "e.description, e.status, e.purchase_date, e.value, e.location " +
                       "FROM EQUIPMENT e " +
                       "JOIN CATEGORIES c ON e.category_id = c.category_id " +
                       "WHERE e.equipment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, equipmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Equipment eq = new Equipment(
                    rs.getInt("equipment_id"),
                    rs.getString("barcode"),
                    rs.getString("equipment_name"),
                    rs.getString("category_name"),
                    rs.getString("status"),
                    rs.getString("location")
                );
                eq.setDescription(rs.getString("description"));
                eq.setPurchaseDate(rs.getString("purchase_date"));
                eq.setValue(rs.getDouble("value"));
                return eq;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving equipment by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get equipment by barcode
     * @param barcode The barcode to search for
     * @return Equipment object if found, null otherwise
     */
    public Equipment getEquipmentByBarcode(String barcode) {
        String query = "SELECT e.equipment_id, e.barcode, e.equipment_name, c.category_name, " +
                       "e.description, e.status, e.purchase_date, e.value, e.location " +
                       "FROM EQUIPMENT e " +
                       "JOIN CATEGORIES c ON e.category_id = c.category_id " +
                       "WHERE e.barcode = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, barcode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Equipment eq = new Equipment(
                    rs.getInt("equipment_id"),
                    rs.getString("barcode"),
                    rs.getString("equipment_name"),
                    rs.getString("category_name"),
                    rs.getString("status"),
                    rs.getString("location")
                );
                eq.setDescription(rs.getString("description"));
                eq.setPurchaseDate(rs.getString("purchase_date"));
                eq.setValue(rs.getDouble("value"));
                return eq;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving equipment by barcode: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all available equipment
     * @return List of available Equipment objects
     */
    public List<Equipment> getAvailableEquipment() {
        return getEquipmentByStatus("AVAILABLE");
    }

    /**
     * Get currently checked out equipment
     * @return List of checked out Equipment objects
     */
    public List<Equipment> getCheckedOutEquipment() {
        return getEquipmentByStatus("CHECKED_OUT");
    }

    /**
     * Get equipment count by status using stored function
     * @param status The status to count
     * @return Number of equipment items with the given status
     */
    public int getEquipmentCountByStatus(String status) {
        String call = "{? = call fn_equipment_status_count(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(call)) {

            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, status);
            cstmt.execute();
            return cstmt.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting equipment by status: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Check if an equipment item is currently available
     * @param equipmentId The equipment ID
     * @return true if available, false otherwise
     */
    public boolean isEquipmentAvailable(int equipmentId) {
        String query = "SELECT status FROM EQUIPMENT WHERE equipment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, equipmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return "AVAILABLE".equals(rs.getString("status"));
            }
        } catch (SQLException e) {
            System.err.println("Error checking equipment availability: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update equipment status
     * @param equipmentId The equipment ID
     * @param status The new status
     * @return true if updated, false otherwise
     */
    public boolean updateEquipmentStatus(int equipmentId, String status) {
        String query = "UPDATE EQUIPMENT SET status = ? WHERE equipment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, equipmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating equipment status: " + e.getMessage());
        }
        return false;
    }
}
