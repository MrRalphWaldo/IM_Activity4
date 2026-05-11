package ui;

import dao.BorrowRecordDAO;
import dao.EquipmentDAO;
import dao.UserDAO;
import models.BorrowRecord;
import models.Equipment;
import models.User;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
                    "9. Check Out Equipment\n" +
                    "10. Log Return Items\n" +
                    "11. Logout\n" +
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
                    checkOutEquipment();
                    break;
                case "10":
                    logReturnItems();
                    break;
                case "11":
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

    /**
     * Check out equipment (full transaction cycle)
     */
    private void checkOutEquipment() {
        System.out.println("\n========== CHECK OUT EQUIPMENT ==========");

        int borrowerId = promptForInt("Enter Borrower ID: ");
        User borrower = userDAO.getActiveUserById(borrowerId);
        if (borrower == null || (!"BORROWER".equals(borrower.getRole()) && !"FACULTY".equals(borrower.getRole()))) {
            System.out.println("Borrower not found or inactive.");
            return;
        }

        String borrowType = promptBorrowType();
        Integer classId = null;
        Integer eventId = null;

        if ("CLASS_ACTIVITY".equals(borrowType)) {
            classId = promptForInt("Enter Class ID: ");
            if (!borrowRecordDAO.classExists(classId)) {
                System.out.println("Class not found.");
                return;
            }
        } else {
            eventId = promptForInt("Enter Event ID: ");
            if (!borrowRecordDAO.eventExists(eventId)) {
                System.out.println("Event not found.");
                return;
            }
        }

        int equipmentId = promptForInt("Enter Equipment ID: ");
        Equipment equipment = equipmentDAO.getEquipmentById(equipmentId);
        if (equipment == null) {
            System.out.println("Equipment not found.");
            return;
        }
        if (!"AVAILABLE".equals(equipment.getStatus())) {
            System.out.println("Equipment is not available for checkout.");
            return;
        }

        int quantity = promptForInt("Enter Quantity: ");
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        String expectedReturnDate = promptForDate("Expected Return Date (YYYY-MM-DD): ");
        String conditionOnCheckout = promptCondition("Condition on Checkout");
        System.out.print("Notes (optional): ");
        String notes = scanner.nextLine().trim();
        if (notes.isEmpty()) {
            notes = null;
        }

        int borrowId = borrowRecordDAO.createBorrowWithProcedure(
                borrowerId,
                currentUser.getUserId(),
                borrowType,
                classId,
                eventId,
                expectedReturnDate,
                notes,
                equipmentId,
                quantity,
                conditionOnCheckout
        );

        if (borrowId > 0) {
            System.out.println("Checkout successful. New Borrow ID: " + borrowId);
        } else {
            System.out.println("Checkout failed. Please review the inputs and try again.");
        }
    }

    /**
     * Log returned items and update equipment status
     */
    private void logReturnItems() {
        System.out.println("\n========== LOG RETURN ITEMS ==========");

        int borrowId = promptForInt("Enter Borrow ID: ");
        if (!borrowRecordDAO.borrowRecordExists(borrowId)) {
            System.out.println("Borrow record not found.");
            return;
        }

        String status = borrowRecordDAO.getBorrowRecordStatus(borrowId);
        if (status == null || "RETURNED".equals(status)) {
            System.out.println("Borrow record is already returned or invalid.");
            return;
        }

        int equipmentId = promptForInt("Enter Equipment ID: ");
        if (!borrowRecordDAO.borrowDetailExists(borrowId, equipmentId)) {
            System.out.println("This equipment is not part of the borrow record.");
            return;
        }

        String conditionOnReturn = promptCondition("Condition on Return");
        System.out.print("Damage notes (optional): ");
        String damageNotes = scanner.nextLine().trim();
        if (damageNotes.isEmpty()) {
            damageNotes = null;
        }

        boolean detailUpdated = borrowRecordDAO.updateBorrowDetailReturn(
                borrowId,
                equipmentId,
                conditionOnReturn,
                damageNotes
        );

        boolean recordUpdated = borrowRecordDAO.markBorrowRecordReturned(borrowId);

        String equipmentStatus = "DAMAGED".equals(conditionOnReturn) ? "MAINTENANCE" : "AVAILABLE";
        boolean equipmentUpdated = equipmentDAO.updateEquipmentStatus(equipmentId, equipmentStatus);

        if (detailUpdated && recordUpdated && equipmentUpdated) {
            System.out.println("Return logged successfully.");
        } else {
            System.out.println("Return update incomplete. Please verify the record.");
        }
    }

    private int promptForInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please try again.");
            }
        }
    }

    private String promptForDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(input);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Date must be today or later.");
                    continue;
                }
                return date.toString();
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }

    private String promptBorrowType() {
        System.out.println("Select Borrow Type:");
        System.out.println("1. CLASS_ACTIVITY");
        System.out.println("2. NON_CLASS_ACTIVITY");
        System.out.print("Select an option: ");

        String choice = scanner.nextLine().trim();
        if ("1".equals(choice)) {
            return "CLASS_ACTIVITY";
        }
        if ("2".equals(choice)) {
            return "NON_CLASS_ACTIVITY";
        }
        System.out.println("Invalid option. Defaulting to CLASS_ACTIVITY.");
        return "CLASS_ACTIVITY";
    }

    private String promptCondition(String label) {
        System.out.println(label + ":");
        System.out.println("1. GOOD");
        System.out.println("2. FAIR");
        System.out.println("3. DAMAGED");
        System.out.print("Select an option: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": return "GOOD";
            case "2": return "FAIR";
            case "3": return "DAMAGED";
            default:
                System.out.println("Invalid option. Defaulting to GOOD.");
                return "GOOD";
        }
    }
}
