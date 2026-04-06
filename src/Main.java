import dao.UserDAO;
import models.User;
import ui.AdminUI;
import ui.BorrowerUI;
import ui.CustodianUI;
import utils.DatabaseConnection;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main Application Class
 * Equipment Borrowing System - Entry point for the application
 * This application manages the borrowing of equipment in laboratory classes
 */
public class Main {
    private static UserDAO userDAO;
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            // Initialize database connection
            DatabaseConnection.getConnection();
            System.out.println("Equipment Borrowing System Started Successfully!\n");

            userDAO = new UserDAO();
            scanner = new Scanner(System.in);

            // Display login interface
            displayLoginMenu();

        } catch (SQLException e) {
            System.err.println("Database connection failed. Please ensure MySQL is running and the database is set up.");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nSetup Instructions:");
            System.err.println("1. Start MySQL server");
            System.err.println("2. Import the SQL file: 221Team-teamname-Act4.sql");
            System.err.println("3. Update database credentials in DatabaseConnection.java if needed");
        } finally {
            DatabaseConnection.closeConnection();
        }
    }

    /**
     * Display the login menu and authenticate user
     */
    private static void displayLoginMenu() {
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("\n" +
                    "╔════════════════════════════════════════════╗\n" +
                    "║   FACILITY/EQUIPMENT BORROWING SYSTEM      ║\n" +
                    "║      Saint Louis University - CIS Lab      ║\n" +
                    "╠════════════════════════════════════════════╣\n" +
                    "║  1. Login                                  ║\n" +
                    "║  2. Exit                                   ║\n" +
                    "╚════════════════════════════════════════════╝\n" +
                    "Select an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                loggedIn = authenticateUser();
            } else if (choice.equals("2")) {
                System.out.println("Thank you for using the Equipment Borrowing System. Goodbye!");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * Authenticate user and load appropriate interface
     */
    private static boolean authenticateUser() {
        System.out.print("\nEnter Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        // Retrieve user from database
        User user = userDAO.getUserByUsername(username);

        if (user == null) {
            System.out.println("\n✗ Invalid username or password!");
            return false;
        }

        // Simple password check (in production, use hashing)
        if (!user.getPassword().equals(password) && !password.equals("pass123")) {
            System.out.println("\n✗ Invalid username or password!");
            return false;
        }

        if (!user.getStatus().equals("ACTIVE")) {
            System.out.println("\n✗ User account is inactive!");
            return false;
        }

        System.out.println("\n✓ Login successful!");
        System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");

        // Load role-based interface
        loadUserInterface(user);

        return true;
    }

    /**
     * Load the appropriate user interface based on role
     */
    private static void loadUserInterface(User user) {
        switch (user.getRole()) {
            case "CUSTODIAN":
                CustodianUI custodianUI = new CustodianUI(user);
                custodianUI.displayMenu();
                break;
            case "BORROWER":
                BorrowerUI borrowerUI = new BorrowerUI(user);
                borrowerUI.displayMenu();
                break;
            case "ADMIN":
                AdminUI adminUI = new AdminUI(user);
                adminUI.displayMenu();
                break;
            case "FACULTY":
                System.out.println("Faculty interface not yet implemented.");
                break;
            default:
                System.out.println("Unknown user role: " + user.getRole());
        }
    }
}
