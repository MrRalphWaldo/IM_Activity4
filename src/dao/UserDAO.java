package dao;

import models.User;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User DAO - Data Access Object for User operations
 * Implements READ operations for retrieving user information
 */
public class UserDAO {

    /**
     * Get all users from the database
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT user_id, username, password, first_name, last_name, email, phone, role, status FROM USERS";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                );
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Get all users by role
     * @param role The role to filter by (CUSTODIAN, BORROWER, ADMIN, FACULTY)
     * @return List of User objects with the specified role
     */
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String query = "SELECT user_id, username, password, first_name, last_name, email, phone, role, status " +
                       "FROM USERS WHERE role = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                );
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users by role: " + e.getMessage());
        }
        return users;
    }

    /**
     * Get a specific user by user ID
     * @param userId The user ID
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        String query = "SELECT user_id, username, password, first_name, last_name, email, phone, role, status " +
                       "FROM USERS WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                );
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get a user by username
     * @param username The username
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        String query = "SELECT user_id, username, password, first_name, last_name, email, phone, role, status " +
                       "FROM USERS WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                );
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all active custodians
     * @return List of active custodians
     */
    public List<User> getActiveCustodians() {
        List<User> custodians = new ArrayList<>();
        String query = "SELECT user_id, username, password, first_name, last_name, email, phone, role, status " +
                       "FROM USERS WHERE role = 'CUSTODIAN' AND status = 'ACTIVE'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                );
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                custodians.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving active custodians: " + e.getMessage());
        }
        return custodians;
    }

    /**
     * Get all active borrowers (students)
     * @return List of active borrowers
     */
    public List<User> getActiveBorrowers() {
        List<User> borrowers = new ArrayList<>();
        String query = "SELECT user_id, username, password, first_name, last_name, email, phone, role, status " +
                       "FROM USERS WHERE role = 'BORROWER' AND status = 'ACTIVE'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                );
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                borrowers.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving active borrowers: " + e.getMessage());
        }
        return borrowers;
    }
}

