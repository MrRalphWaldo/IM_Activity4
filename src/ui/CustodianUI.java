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
 * Custodian UI - Interface for custodian operations
 * Allows custodians to view equipment, manage borrow records, and check returns
 */
public class CustodianUI {
    private UserDAO userDAO;
    private EquipmentDAO equipmentDAO;
    private BorrowRecordDAO borrowRecordDAO;
    private Scanner scanner;
    private User currentUser;

    public CustodianUI(User currentUser) {
        this.currentUser = currentUser;
        this.userDAO = new UserDAO();
        this.equipmentDAO = new EquipmentDAO();
        this.borrowRecordDAO = new BorrowRecordDAO();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display main custodian menu
     */
    public void displayMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" +
                    "===========================================\n" +
                    "    CUSTODIAN INTERFACE - Equipment Management\n" +
                    "===========================================\n" +
                    "Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + "\n" +
                    "1. View All Equipment\n" +
                    "2. View Available Equipment\n" +
                    "3. View Checked Out Equipment\n" +
                    "4. Search Equipment by Barcode\n" +
                    "5. View All Borrow Records\n" +
                    "6. View Unreturned Items\n" +
                    "7. View Borrow History by Class\n" +
                    "8. View Borrow Details by Borrower\n" +
                    "9. Logout\n" +
                    "===========================================\n" +
                    "Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllEquipment();
                    break;
                case "2":
                    viewAvailableEquipment();
                    break;
                case "3":
                    viewCheckedOutEquipment();
                    break;
                case "4":
                    searchEquipmentByBarcode();
                    break;
                case "5":
                    viewAllBorrowRecords();
                    break;
                case "6":
                    viewUnreturnedItems();
                    break;
                case "7":
                    viewBorrowHistoryByClass();
                    break;
                case "8":
                    viewBorrowDetailsByBorrower();
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
     * View all equipment in the system
     */
    private void viewAllEquipment() {
        System.out.println("\n========== ALL EQUIPMENT ==========");
        List<Equipment> equipment = equipmentDAO.getAllEquipment();

        if (equipment.isEmpty()) {
            System.out.println("No equipment found.");
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
     * View only available equipment
     */
    private void viewAvailableEquipment() {
        System.out.println("\n========== AVAILABLE EQUIPMENT ==========");
        List<Equipment> equipment = equipmentDAO.getAvailableEquipment();

        if (equipment.isEmpty()) {
            System.out.println("No available equipment at this time.");
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
     * View currently checked out equipment
     */
    private void viewCheckedOutEquipment() {
        System.out.println("\n========== CHECKED OUT EQUIPMENT ==========");
        List<Equipment> equipment = equipmentDAO.getCheckedOutEquipment();

        if (equipment.isEmpty()) {
            System.out.println("No checked out equipment at this time.");
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
     * Search for equipment by barcode
     */
    private void searchEquipmentByBarcode() {
        System.out.print("\nEnter barcode to search: ");
        String barcode = scanner.nextLine().trim();

        Equipment equipment = equipmentDAO.getEquipmentByBarcode(barcode);

        if (equipment == null) {
            System.out.println("Equipment with barcode '" + barcode + "' not found.");
            return;
        }

        System.out.println("\n========== EQUIPMENT DETAILS ==========");
        System.out.println("ID:              " + equipment.getEquipmentId());
        System.out.println("Barcode:         " + equipment.getBarcode());
        System.out.println("Name:            " + equipment.getEquipmentName());
        System.out.println("Category:        " + equipment.getCategoryName());
        System.out.println("Description:     " + equipment.getDescription());
        System.out.println("Status:          " + equipment.getStatus());
        System.out.println("Purchase Date:   " + equipment.getPurchaseDate());
        System.out.println("Value:           PHP " + String.format("%.2f", equipment.getValue()));
        System.out.println("Location:        " + equipment.getLocation());
        System.out.println("=====================================");
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
     * View borrow history by class
     */
    private void viewBorrowHistoryByClass() {
        System.out.print("\nEnter Class ID: ");
        try {
            int classId = Integer.parseInt(scanner.nextLine().trim());
            List<BorrowRecord> records = borrowRecordDAO.getBorrowRecordsByClass(classId);

            if (records.isEmpty()) {
                System.out.println("No borrow records found for this class.");
                return;
            }

            System.out.println("\n========== BORROW HISTORY FOR CLASS ==========");
            System.out.printf("%-5s %-20s %-20s %-15s %-12s\n",
                    "ID", "Borrower", "Date", "Type", "Status");
            System.out.println("-".repeat(90));

            for (BorrowRecord record : records) {
                System.out.printf("%-5d %-20s %-20s %-15s %-12s\n",
                        record.getBorrowId(),
                        record.getBorrowerName(),
                        record.getBorrowDate(),
                        record.getBorrowType(),
                        record.getReturnStatus());
            }
            System.out.println("-".repeat(90));
        } catch (NumberFormatException e) {
            System.out.println("Invalid Class ID format.");
        }
    }

    /**
     * View borrow details by borrower
     */
    private void viewBorrowDetailsByBorrower() {
        System.out.print("\nEnter Borrower ID: ");
        try {
            int borrowerId = Integer.parseInt(scanner.nextLine().trim());
            List<BorrowRecord> records = borrowRecordDAO.getBorrowRecordsByBorrower(borrowerId);

            if (records.isEmpty()) {
                System.out.println("No borrow records found for this borrower.");
                return;
            }

            System.out.println("\n========== BORROW HISTORY FOR BORROWER ==========");
            System.out.printf("%-5s %-20s %-15s %-15s %-12s\n",
                    "ID", "Activity", "Borrow Date", "Expected Return", "Status");
            System.out.println("-".repeat(90));

            for (BorrowRecord record : records) {
                System.out.printf("%-5d %-20s %-15s %-15s %-12s\n",
                        record.getBorrowId(),
                        record.getClassName() != null ? record.getClassName() : "N/A",
                        record.getBorrowDate(),
                        record.getExpectedReturnDate(),
                        record.getReturnStatus());
            }
            System.out.println("-".repeat(90));
        } catch (NumberFormatException e) {
            System.out.println("Invalid Borrower ID format.");
        }
    }
}

