package ui;

import dao.BorrowRecordDAO;
import dao.EquipmentDAO;
import dao.UserDAO;
import models.BorrowRecord;
import models.Equipment;
import models.User;

import java.util.List;
import java.util.Scanner;

/**
 * Admin UI - Interface for administrator operations
 * Allows admins to view equipment status and borrow records
 */
public class AdminUI {
    private UserDAO userDAO;
    private EquipmentDAO equipmentDAO;
    private BorrowRecordDAO borrowRecordDAO;
    private Scanner scanner;
    private User currentUser;

    public AdminUI(User currentUser) {
        this.currentUser = currentUser;
        this.userDAO = new UserDAO();
        this.equipmentDAO = new EquipmentDAO();
        this.borrowRecordDAO = new BorrowRecordDAO();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display main admin menu
     */
    public void displayMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" +
                    "===========================================\n" +
                    "       ADMIN INTERFACE - System Management\n" +
                    "===========================================\n" +
                    "Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + "\n" +
                    "1. View Equipment Status\n" +
                    "2. View Equipment by Status\n" +
                    "3. View All Borrow Records\n" +
                    "4. View Borrow Records by Status\n" +
                    "5. View Unreturned Items\n" +
                    "6. View Custodian List\n" +
                    "7. View Borrower List\n" +
                    "8. View Faculty/Instructor List\n" +
                    "9. Logout\n" +
                    "===========================================\n" +
                    "Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewEquipmentStatus();
                    break;
                case "2":
                    viewEquipmentByStatus();
                    break;
                case "3":
                    viewAllBorrowRecords();
                    break;
                case "4":
                    viewBorrowRecordsByStatus();
                    break;
                case "5":
                    viewUnreturnedItems();
                    break;
                case "6":
                    viewCustodianList();
                    break;
                case "7":
                    viewBorrowerList();
                    break;
                case "8":
                    viewFacultyList();
                    break;
                case "9":
                    running = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * View complete equipment status summary
     */
    private void viewEquipmentStatus() {
        System.out.println("\n========== EQUIPMENT STATUS SUMMARY ==========");
        List<Equipment> allEquipment = equipmentDAO.getAllEquipment();

        if (allEquipment.isEmpty()) {
            System.out.println("No equipment found.");
            return;
        }

        int available = 0, checkedOut = 0, maintenance = 0, retired = 0;

        for (Equipment eq : allEquipment) {
            switch (eq.getStatus()) {
                case "AVAILABLE": available++; break;
                case "CHECKED_OUT": checkedOut++; break;
                case "MAINTENANCE": maintenance++; break;
                case "RETIRED": retired++; break;
            }
        }

        System.out.println("Total Equipment:        " + allEquipment.size());
        System.out.println("Available:              " + available);
        System.out.println("Checked Out:            " + checkedOut);
        System.out.println("Under Maintenance:      " + maintenance);
        System.out.println("Retired:                " + retired);
        System.out.println("============================================");
    }

    /**
     * View equipment filtered by status
     */
    private void viewEquipmentByStatus() {
        System.out.println("\nSelect Status:");
        System.out.println("1. AVAILABLE");
        System.out.println("2. CHECKED_OUT");
        System.out.println("3. MAINTENANCE");
        System.out.println("4. RETIRED");
        System.out.print("Select an option: ");

        String choice = scanner.nextLine().trim();
        String status = "";

        switch (choice) {
            case "1": status = "AVAILABLE"; break;
            case "2": status = "CHECKED_OUT"; break;
            case "3": status = "MAINTENANCE"; break;
            case "4": status = "RETIRED"; break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        List<Equipment> equipment = equipmentDAO.getEquipmentByStatus(status);

        System.out.println("\n========== EQUIPMENT - " + status + " ==========");

        if (equipment.isEmpty()) {
            System.out.println("No equipment found with status: " + status);
            return;
        }

        System.out.printf("%-5s %-10s %-20s %-20s %-15s %-20s\n",
                "ID", "Barcode", "Equipment Name", "Category", "Status", "Location");
        System.out.println("-".repeat(100));

        for (Equipment eq : equipment) {
            System.out.printf("%-5d %-10s %-20s %-20s %-15s %-20s\n",
                    eq.getEquipmentId(),
                    eq.getBarcode(),
                    eq.getEquipmentName(),
                    eq.getCategoryName(),
                    eq.getStatus(),
                    eq.getLocation());
        }
        System.out.println("-".repeat(100));
    }

    /**
     * View all borrow records
     */
    private void viewAllBorrowRecords() {
        System.out.println("\n========== ALL BORROW RECORDS ==========");
        List<BorrowRecord> records = borrowRecordDAO.getAllBorrowRecords();

        if (records.isEmpty()) {
            System.out.println("No borrow records found.");
            return;
        }

        System.out.printf("%-5s %-20s %-20s %-20s %-15s %-12s\n",
                "ID", "Borrower", "Custodian", "Activity", "Type", "Status");
        System.out.println("-".repeat(110));

        for (BorrowRecord record : records) {
            System.out.printf("%-5d %-20s %-20s %-20s %-15s %-12s\n",
                    record.getBorrowId(),
                    record.getBorrowerName(),
                    record.getCustodianName(),
                    record.getClassName() != null ? record.getClassName() : "N/A",
                    record.getBorrowType(),
                    record.getReturnStatus());
        }
        System.out.println("-".repeat(110));
    }

    /**
     * View borrow records by return status
     */
    private void viewBorrowRecordsByStatus() {
        System.out.println("\nSelect Return Status:");
        System.out.println("1. ACTIVE");
        System.out.println("2. RETURNED");
        System.out.println("3. OVERDUE");
        System.out.print("Select an option: ");

        String choice = scanner.nextLine().trim();
        String status = "";

        switch (choice) {
            case "1": status = "ACTIVE"; break;
            case "2": status = "RETURNED"; break;
            case "3": status = "OVERDUE"; break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        List<BorrowRecord> records = borrowRecordDAO.getBorrowRecordsByStatus(status);

        System.out.println("\n========== BORROW RECORDS - " + status + " ==========");

        if (records.isEmpty()) {
            System.out.println("No borrow records found with status: " + status);
            return;
        }

        System.out.printf("%-5s %-20s %-20s %-20s %-15s %-12s\n",
                "ID", "Borrower", "Custodian", "Activity", "Type", "Status");
        System.out.println("-".repeat(110));

        for (BorrowRecord record : records) {
            System.out.printf("%-5d %-20s %-20s %-20s %-15s %-12s\n",
                    record.getBorrowId(),
                    record.getBorrowerName(),
                    record.getCustodianName(),
                    record.getClassName() != null ? record.getClassName() : "N/A",
                    record.getBorrowType(),
                    record.getReturnStatus());
        }
        System.out.println("-".repeat(110));
    }

    /**
     * View unreturned items
     */
    private void viewUnreturnedItems() {
        System.out.println("\n========== UNRETURNED ITEMS ==========");
        List<BorrowRecord> records = borrowRecordDAO.getUnreturnedRecords();

        if (records.isEmpty()) {
            System.out.println("All items have been returned!");
            return;
        }

        System.out.printf("%-5s %-20s %-20s %-15s %-12s\n",
                "ID", "Borrower", "Activity", "Expected Return", "Status");
        System.out.println("-".repeat(90));

        for (BorrowRecord record : records) {
            System.out.printf("%-5d %-20s %-20s %-15s %-12s\n",
                    record.getBorrowId(),
                    record.getBorrowerName(),
                    record.getClassName() != null ? record.getClassName() : "N/A",
                    record.getExpectedReturnDate(),
                    record.getReturnStatus());
        }
        System.out.println("-".repeat(90));
    }

    /**
     * View all custodians
     */
    private void viewCustodianList() {
        System.out.println("\n========== CUSTODIAN LIST ==========");
        List<User> custodians = userDAO.getActiveCustodians();

        if (custodians.isEmpty()) {
            System.out.println("No active custodians found.");
            return;
        }

        System.out.printf("%-5s %-20s %-30s %-25s %-10s\n",
                "ID", "Username", "Name", "Email", "Status");
        System.out.println("-".repeat(100));

        for (User user : custodians) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            System.out.printf("%-5d %-20s %-30s %-25s %-10s\n",
                    user.getUserId(),
                    user.getUsername(),
                    fullName,
                    user.getEmail(),
                    user.getStatus());
        }
        System.out.println("-".repeat(100));
    }

    /**
     * View all borrowers (students)
     */
    private void viewBorrowerList() {
        System.out.println("\n========== BORROWER LIST ==========");
        List<User> borrowers = userDAO.getActiveBorrowers();

        if (borrowers.isEmpty()) {
            System.out.println("No active borrowers found.");
            return;
        }

        System.out.printf("%-5s %-20s %-30s %-25s %-10s\n",
                "ID", "Username", "Name", "Email", "Status");
        System.out.println("-".repeat(100));

        for (User user : borrowers) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            System.out.printf("%-5d %-20s %-30s %-25s %-10s\n",
                    user.getUserId(),
                    user.getUsername(),
                    fullName,
                    user.getEmail(),
                    user.getStatus());
        }
        System.out.println("-".repeat(100));
    }

    /**
     * View all faculty/instructors
     */
    private void viewFacultyList() {
        System.out.println("\n========== FACULTY/INSTRUCTOR LIST ==========");
        List<User> faculty = userDAO.getUsersByRole("FACULTY");

        if (faculty.isEmpty()) {
            System.out.println("No faculty members found.");
            return;
        }

        System.out.printf("%-5s %-20s %-30s %-25s %-10s\n",
                "ID", "Username", "Name", "Email", "Status");
        System.out.println("-".repeat(100));

        for (User user : faculty) {
            String fullName = user.getFirstName() + " " + user.getLastName();
            System.out.printf("%-5d %-20s %-30s %-25s %-10s\n",
                    user.getUserId(),
                    user.getUsername(),
                    fullName,
                    user.getEmail(),
                    user.getStatus());
        }
        System.out.println("-".repeat(100));
    }
}

