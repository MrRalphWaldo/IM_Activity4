package models;

import java.time.LocalDateTime;

/**
 * BorrowRecord Model - Represents a borrowing transaction
 */
public class BorrowRecord {
    private int borrowId;
    private int borrowerId;
    private String borrowerName;
    private int custodianId;
    private String custodianName;
    private Integer classId;
    private String className;
    private Integer eventId;
    private String eventName;
    private String borrowType; // CLASS_ACTIVITY, NON_CLASS_ACTIVITY
    private LocalDateTime borrowDate;
    private String expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private String returnStatus; // ACTIVE, RETURNED, OVERDUE
    private String notes;

    // Constructors
    public BorrowRecord() {}

    public BorrowRecord(int borrowId, String borrowerName, String custodianName,
                        String className, String borrowType, LocalDateTime borrowDate,
                        String expectedReturnDate, String returnStatus) {
        this.borrowId = borrowId;
        this.borrowerName = borrowerName;
        this.custodianName = custodianName;
        this.className = className;
        this.borrowType = borrowType;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returnStatus = returnStatus;
    }

    // Getters and Setters
    public int getBorrowId() { return borrowId; }
    public void setBorrowId(int borrowId) { this.borrowId = borrowId; }

    public int getBorrowerId() { return borrowerId; }
    public void setBorrowerId(int borrowerId) { this.borrowerId = borrowerId; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public int getCustodianId() { return custodianId; }
    public void setCustodianId(int custodianId) { this.custodianId = custodianId; }

    public String getCustodianName() { return custodianName; }
    public void setCustodianName(String custodianName) { this.custodianName = custodianName; }

    public Integer getClassId() { return classId; }
    public void setClassId(Integer classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getBorrowType() { return borrowType; }
    public void setBorrowType(String borrowType) { this.borrowType = borrowType; }

    public LocalDateTime getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDateTime borrowDate) { this.borrowDate = borrowDate; }

    public String getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(String expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public LocalDateTime getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public String getReturnStatus() { return returnStatus; }
    public void setReturnStatus(String returnStatus) { this.returnStatus = returnStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "borrowId=" + borrowId +
                ", borrowerName='" + borrowerName + '\'' +
                ", custodianName='" + custodianName + '\'' +
                ", borrowType='" + borrowType + '\'' +
                ", returnStatus='" + returnStatus + '\'' +
                '}';
    }
}

