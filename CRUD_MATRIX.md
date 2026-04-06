# CRUD Matrix - Facility/Equipment Borrowing System

## Overview
This matrix documents all user interfaces (UIs), actors/users, and their corresponding CRUD operations in the Equipment Borrowing System.

---

## CRUD Operations Definition
- **C (Create)**: Insert new records into the database
- **R (Read)**: Retrieve/query data from the database
- **U (Update)**: Modify existing records
- **D (Delete)**: Remove records (used sparingly)

---

## CRUD MATRIX

### 1. CUSTODIAN INTERFACE

| UI Screen | Actor(s) | Operation | Tables/Columns Involved | Operation Type | Description |
|-----------|----------|-----------|------------------------|-----------------|------------|
| View All Equipment | Custodian | R | EQUIPMENT (all columns) | READ | Retrieve all equipment in the system with full details |
| View Available Equipment | Custodian | R | EQUIPMENT WHERE status='AVAILABLE' | READ | Display only currently available equipment |
| View Checked Out Equipment | Custodian | R | EQUIPMENT WHERE status='CHECKED_OUT' | READ | Show equipment currently in use |
| Search Equipment by Barcode | Custodian | R | EQUIPMENT WHERE barcode=? | READ | Find equipment by scanning/entering barcode |
| View All Borrow Records | Custodian | R | BORROW_RECORDS JOIN USERS JOIN LABORATORY_CLASSES/EVENTS | READ | Display all borrowing transactions with borrower and activity info |
| View Unreturned Items | Custodian | R | BORROW_RECORDS WHERE return_status IN ('ACTIVE','OVERDUE') | READ | Identify items not yet returned or overdue |
| View Borrow History by Class | Custodian | R | BORROW_RECORDS WHERE class_id=? JOIN related tables | READ | Show all borrowing activity for a specific lab class |
| View Borrow Details by Borrower | Custodian | R | BORROW_RECORDS WHERE borrower_id=? | READ | Display borrowing history of a specific student/borrower |
| Check Out Equipment | Custodian | C | BORROW_RECORDS (borrower_id, custodian_id, class_id, borrow_date, expected_return_date, borrow_type) | CREATE | Record a new equipment checkout transaction |
| Check Out Equipment | Custodian | C | BORROW_DETAILS (borrow_id, equipment_id, condition_on_checkout) | CREATE | Record which specific items are checked out |
| Log Return Items | Custodian | U | BORROW_RECORDS (actual_return_date, return_status) | UPDATE | Update borrow record with return information |
| Log Return Items | Custodian | U | BORROW_DETAILS (condition_on_return, damage_notes) | UPDATE | Record item condition upon return and note any issues |
| Log Return Items | Custodian | U | EQUIPMENT (status) | UPDATE | Update equipment status back to AVAILABLE or MAINTENANCE |

---

### 2. BORROWER INTERFACE

| UI Screen | Actor(s) | Operation | Tables/Columns Involved | Operation Type | Description |
|-----------|----------|-----------|------------------------|-----------------|------------|
| View My Borrow History | Borrower (Student/Faculty) | R | BORROW_RECORDS WHERE borrower_id=? | READ | Display personal borrowing transaction history |
| View All Transactions | Borrower (Student/Faculty) | R | BORROW_RECORDS JOIN USERS JOIN CLASSES/EVENTS | READ | View all system borrow records (for transparency) |
| Input Borrowed Items | Borrower | C | BORROW_RECORDS | CREATE | Create new borrow request/transaction |
| Input Borrowed Items | Borrower | C | BORROW_DETAILS | CREATE | Record specific items being borrowed |

---

### 3. ADMINISTRATOR INTERFACE

| UI Screen | Actor(s) | Operation | Tables/Columns Involved | Operation Type | Description |
|-----------|----------|-----------|------------------------|-----------------|------------|
| View Equipment Status | Admin | R | EQUIPMENT (COUNT by status) | READ | Get summary statistics of equipment status distribution |
| View Equipment by Status | Admin | R | EQUIPMENT WHERE status=? | READ | Filter and display equipment by specific status |
| View All Borrow Records | Admin | R | BORROW_RECORDS with all JOIN operations | READ | Access complete borrowing history for reporting |
| View Borrow Records by Status | Admin | R | BORROW_RECORDS WHERE return_status=? | READ | Filter borrow records by return status |
| View Unreturned Items | Admin | R | BORROW_RECORDS WHERE return_status IN ('ACTIVE','OVERDUE') | READ | Monitor items requiring follow-up |
| View Custodian List | Admin | R | USERS WHERE role='CUSTODIAN' AND status='ACTIVE' | READ | List all active custodian accounts |
| View Borrower List | Admin | R | USERS WHERE role='BORROWER' AND status='ACTIVE' | READ | List all active borrower accounts |
| View Faculty List | Admin | R | USERS WHERE role='FACULTY' | READ | Display faculty/instructor accounts |
| Manage Custodian Accounts | Admin | U | USERS (status, password, email, phone) | UPDATE | Modify custodian user account details |
| Manage Custodian Accounts | Admin | U | USERS SET status='INACTIVE' | UPDATE | Deactivate custodian accounts |
| Manage Custodian Accounts | Admin | C | USERS (username, password, first_name, last_name, email, role) | CREATE | Add new custodian user account |

---

## Data Access Objects (DAOs) Implemented

### UserDAO - READ Operations
- `getAllUsers()` - Retrieve all system users
- `getUsersByRole(role)` - Get users filtered by role
- `getUserById(userId)` - Get specific user by ID
- `getUserByUsername(username)` - Get user by username (for login)
- `getActiveCustodians()` - Get all active custodians
- `getActiveBorrowers()` - Get all active borrowers

### EquipmentDAO - READ Operations
- `getAllEquipment()` - Retrieve all equipment
- `getEquipmentByStatus(status)` - Filter equipment by status
- `getEquipmentByCategory(categoryId)` - Filter by category
- `getEquipmentById(equipmentId)` - Get specific equipment
- `getEquipmentByBarcode(barcode)` - Search by barcode
- `getAvailableEquipment()` - Get only available items
- `getCheckedOutEquipment()` - Get only checked out items

### BorrowRecordDAO - READ Operations
- `getAllBorrowRecords()` - Retrieve all borrow transactions
- `getBorrowRecordsByStatus(status)` - Filter by return status
- `getBorrowRecordsByBorrower(borrowerId)` - Get borrower's history
- `getBorrowRecordsByClass(classId)` - Get class activity history
- `getUnreturnedRecords()` - Get active/unreturned items
- `getBorrowRecordById(borrowId)` - Get specific transaction details

---

