package ui;

import dao.BorrowRecordDAO;
import dao.UserDAO;
import models.BorrowRecord;
import models.User;

import java.util.List;
import java.util.Scanner;

/**
 * Borrower UI - Interface for borrower/student operations
 * Allows borrowers to view their borrow history
 */
public class BorrowerUI {
    private UserDAO userDAO;
    private BorrowRecordDAO borrowRecordDAO;
    private Scanner scanner;
    private User currentUser;

    public BorrowerUI(User currentUser) {
        this.currentUser = currentUser;
        this.userDAO = new UserDAO();
        this.borrowRecordDAO = new BorrowRecordDAO();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display main borrower menu
     */
    public void displayMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n" +
                    "===========================================\n" +
                    "       BORROWER INTERFACE - View Borrow History\n" +
                    "===========================================\n" +
                    "Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + "\n" +
                    "1. View My Borrow History\n" +
                    "2. View All Transactions\n" +
                    "3. Logout\n" +
                    "===========================================\n" +
                    "Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewMyBorrowHistory();
                    break;
                case "2":
                    viewAllTransactions();
                    break;
                case "3":
                    running = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * View personal borrow history
     */
    private void viewMyBorrowHistory() {
        System.out.println("\n========== MY BORROW HISTORY ==========");
        List<BorrowRecord> records = borrowRecordDAO.getBorrowRecordsByBorrower(currentUser.getUserId());

        if (records.isEmpty()) {
            System.out.println("You have no borrow history.");
            return;
        }

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
    }

    /**
     * View all borrow transactions in the system (READ operation)
     */
    private void viewAllTransactions() {
        System.out.println("\n========== ALL BORROW TRANSACTIONS ==========");
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
}

