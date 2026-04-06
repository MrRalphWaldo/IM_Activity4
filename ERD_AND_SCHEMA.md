# Entity Relationship Diagram (ERD) & Relational Schema
## Facility/Equipment Borrowing System

---

## ERD Description

### Entity Types

#### Strong Entities:
1. **USERS** - All system users (Custodian, Borrower, Admin, Faculty)
2. **EQUIPMENT** - Lab equipment and accessories with barcodes
3. **CATEGORIES** - Equipment categories/classifications
4. **LABORATORY_CLASSES** - CIS lab classes with students
5. **EVENTS** - Non-class activities (training, recruitment, exams, etc.)

#### Weak Entity:
1. **STUDENTS** - Student profiles (depends on USERS for identification)

#### Associative Entities (Many-to-Many):
1. **CLASS_ENROLLMENT** - Links STUDENTS to LABORATORY_CLASSES
2. **BORROW_DETAILS** - Links BORROW_RECORDS to EQUIPMENT (itemized list)

#### Relationship Entities:
1. **BORROW_RECORDS** - Main borrowing transaction records

---

## Relationships in ERD

### 1. USERS ↔ STUDENTS (1:1)
- **Type**: Identifying relationship (weak entity)
- **Cardinality**: One User can have zero or one Student record
- **Foreign Key**: STUDENTS.user_id references USERS.user_id
- **Constraint**: ON DELETE CASCADE (student record removed if user deleted)

### 2. USERS ↔ LABORATORY_CLASSES (1:N) - "INSTRUCTS"
- **Type**: Relationship
- **Cardinality**: One Faculty/Instructor can instruct many classes
- **Foreign Key**: LABORATORY_CLASSES.instructor_id references USERS.user_id
- **Constraint**: ON DELETE SET NULL (class instructor can be null if user deleted)

### 3. CATEGORIES ↔ EQUIPMENT (1:N)
- **Type**: Relationship
- **Cardinality**: One Category has many Equipment items
- **Foreign Key**: EQUIPMENT.category_id references CATEGORIES.category_id
- **Constraint**: ON DELETE RESTRICT (cannot delete category with equipment)

### 4. LABORATORY_CLASSES ↔ STUDENTS (M:N) - "ENROLLS_IN"
- **Type**: Associative entity relationship
- **Cardinality**: Many Students enroll in many Classes
- **Bridge Table**: CLASS_ENROLLMENT
- **Foreign Keys**:
  - CLASS_ENROLLMENT.student_id references STUDENTS.student_id
  - CLASS_ENROLLMENT.class_id references LABORATORY_CLASSES.class_id
- **Constraint**: ON DELETE CASCADE (enrollment removed if student/class deleted)

### 5. USERS ↔ BORROW_RECORDS (1:N) - "BORROWS" (as Borrower)
- **Type**: Relationship
- **Cardinality**: One Borrower can have many borrow records
- **Foreign Key**: BORROW_RECORDS.borrower_id references USERS.user_id
- **Constraint**: ON DELETE CASCADE

### 6. USERS ↔ BORROW_RECORDS (1:N) - "MANAGES" (as Custodian)
- **Type**: Relationship
- **Cardinality**: One Custodian manages many borrow records
- **Foreign Key**: BORROW_RECORDS.custodian_id references USERS.user_id
- **Constraint**: ON DELETE RESTRICT (custodian required for transaction records)

### 7. LABORATORY_CLASSES ↔ BORROW_RECORDS (1:N)
- **Type**: Relationship
- **Cardinality**: One Class has many borrow records
- **Foreign Key**: BORROW_RECORDS.class_id references LABORATORY_CLASSES.class_id
- **Constraint**: ON DELETE SET NULL (class activity optional)

### 8. EVENTS ↔ BORROW_RECORDS (1:N)
- **Type**: Relationship
- **Cardinality**: One Event has many associated borrow records
- **Foreign Key**: BORROW_RECORDS.event_id references EVENTS.event_id
- **Constraint**: ON DELETE SET NULL (event activity optional)

### 9. BORROW_RECORDS ↔ EQUIPMENT (M:N) - "CONTAINS"
- **Type**: Associative entity relationship
- **Cardinality**: One borrow record contains many equipment items
- **Bridge Table**: BORROW_DETAILS
- **Foreign Keys**:
  - BORROW_DETAILS.borrow_id references BORROW_RECORDS.borrow_id
  - BORROW_DETAILS.equipment_id references EQUIPMENT.equipment_id
- **Constraint**: ON DELETE CASCADE (details removed if borrow record deleted)

### 10. USERS ↔ EVENTS (1:N) - "ORGANIZES"
- **Type**: Relationship
- **Cardinality**: One User can organize many events
- **Foreign Key**: EVENTS.organizer_id references USERS.user_id
- **Constraint**: ON DELETE CASCADE

### 11. USERS ↔ EVENTS (1:N) - "APPROVES" (as Admin)
- **Type**: Relationship
- **Cardinality**: One Admin approves many events
- **Foreign Key**: EVENTS.approved_by references USERS.user_id
- **Constraint**: ON DELETE SET NULL

---

## Relational Schema

### 1. USERS Table
```
USERS (
    user_id: INT [PK, AUTO_INCREMENT],
    username: VARCHAR(50) [UNIQUE, NOT NULL],
    password: VARCHAR(255) [NOT NULL],
    first_name: VARCHAR(100) [NOT NULL],
    last_name: VARCHAR(100) [NOT NULL],
    email: VARCHAR(100) [UNIQUE, NOT NULL],
    phone: VARCHAR(20),
    role: ENUM('CUSTODIAN', 'BORROWER', 'ADMIN', 'FACULTY') [NOT NULL],
    status: ENUM('ACTIVE', 'INACTIVE') [DEFAULT 'ACTIVE'],
    created_date: TIMESTAMP [DEFAULT CURRENT_TIMESTAMP],
    CONSTRAINT: username length >= 3
    CONSTRAINT: email format validation (LIKE '%@%.%')
)
```

### 2. STUDENTS Table (Weak Entity)
```
STUDENTS (
    student_id: INT [PK],
    user_id: INT [FK→USERS.user_id] [UNIQUE, NOT NULL],
    student_number: VARCHAR(20) [UNIQUE, NOT NULL],
    course: VARCHAR(50),
    year_level: INT [CHECK 1-4],
    FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE
)
```

### 3. CATEGORIES Table
```
CATEGORIES (
    category_id: INT [PK, AUTO_INCREMENT],
    category_name: VARCHAR(100) [UNIQUE, NOT NULL],
    description: TEXT,
    created_date: TIMESTAMP [DEFAULT CURRENT_TIMESTAMP]
)
```

### 4. EQUIPMENT Table
```
EQUIPMENT (
    equipment_id: INT [PK, AUTO_INCREMENT],
    barcode: VARCHAR(50) [UNIQUE, NOT NULL],
    equipment_name: VARCHAR(100) [NOT NULL],
    category_id: INT [FK→CATEGORIES.category_id] [NOT NULL],
    description: TEXT,
    status: ENUM('AVAILABLE', 'CHECKED_OUT', 'MAINTENANCE', 'RETIRED')
        [DEFAULT 'AVAILABLE'],
    purchase_date: DATE,
    value: DECIMAL(10, 2) [CHECK > 0],
    location: VARCHAR(100),
    created_date: TIMESTAMP [DEFAULT CURRENT_TIMESTAMP],
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id) 
        ON DELETE RESTRICT
)
```

### 5. LABORATORY_CLASSES Table
```
LABORATORY_CLASSES (
    class_id: INT [PK, AUTO_INCREMENT],
    class_code: VARCHAR(20) [UNIQUE, NOT NULL],
    class_name: VARCHAR(100) [NOT NULL],
    instructor_id: INT [FK→USERS.user_id],
    semester: VARCHAR(10),
    academic_year: VARCHAR(10),
    time_slot: VARCHAR(50),
    room_location: VARCHAR(50),
    FOREIGN KEY (instructor_id) REFERENCES USERS(user_id) 
        ON DELETE SET NULL
)
```

### 6. CLASS_ENROLLMENT Table (Associative Entity)
```
CLASS_ENROLLMENT (
    enrollment_id: INT [PK, AUTO_INCREMENT],
    student_id: INT [FK→STUDENTS.student_id] [NOT NULL],
    class_id: INT [FK→LABORATORY_CLASSES.class_id] [NOT NULL],
    enrollment_date: DATE [DEFAULT CURDATE()],
    UNIQUE KEY (student_id, class_id),
    FOREIGN KEY (student_id) REFERENCES STUDENTS(student_id) 
        ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES LABORATORY_CLASSES(class_id) 
        ON DELETE CASCADE
)
```

### 7. EVENTS Table
```
EVENTS (
    event_id: INT [PK, AUTO_INCREMENT],
    event_name: VARCHAR(100) [NOT NULL],
    event_type: ENUM('TRAINING', 'RECRUITMENT', 'CERTIFICATION_EXAM', 
        'MEETING', 'LECTURE', 'OTHER') [NOT NULL],
    organizer_id: INT [FK→USERS.user_id] [NOT NULL],
    event_date: DATE [NOT NULL],
    start_time: TIME,
    end_time: TIME,
    location: VARCHAR(100),
    description: TEXT,
    approval_status: ENUM('PENDING', 'APPROVED', 'REJECTED') 
        [DEFAULT 'PENDING'],
    approved_by: INT [FK→USERS.user_id],
    approval_date: DATETIME,
    created_date: TIMESTAMP [DEFAULT CURRENT_TIMESTAMP],
    FOREIGN KEY (organizer_id) REFERENCES USERS(user_id) 
        ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES USERS(user_id) 
        ON DELETE SET NULL
)
```

### 8. BORROW_RECORDS Table
```
BORROW_RECORDS (
    borrow_id: INT [PK, AUTO_INCREMENT],
    borrower_id: INT [FK→USERS.user_id] [NOT NULL],
    custodian_id: INT [FK→USERS.user_id] [NOT NULL],
    class_id: INT [FK→LABORATORY_CLASSES.class_id],
    event_id: INT [FK→EVENTS.event_id],
    borrow_type: ENUM('CLASS_ACTIVITY', 'NON_CLASS_ACTIVITY') [NOT NULL],
    borrow_date: DATETIME [DEFAULT CURRENT_TIMESTAMP],
    expected_return_date: DATE [NOT NULL, CHECK >= CURDATE()],
    actual_return_date: DATETIME,
    return_status: ENUM('ACTIVE', 'RETURNED', 'OVERDUE') 
        [DEFAULT 'ACTIVE'],
    notes: TEXT,
    FOREIGN KEY (borrower_id) REFERENCES USERS(user_id) 
        ON DELETE CASCADE,
    FOREIGN KEY (custodian_id) REFERENCES USERS(user_id) 
        ON DELETE RESTRICT,
    FOREIGN KEY (class_id) REFERENCES LABORATORY_CLASSES(class_id) 
        ON DELETE SET NULL,
    FOREIGN KEY (event_id) REFERENCES EVENTS(event_id) 
        ON DELETE SET NULL
)
```

### 9. BORROW_DETAILS Table (Associative Entity)
```
BORROW_DETAILS (
    detail_id: INT [PK, AUTO_INCREMENT],
    borrow_id: INT [FK→BORROW_RECORDS.borrow_id] [NOT NULL],
    equipment_id: INT [FK→EQUIPMENT.equipment_id] [NOT NULL],
    quantity: INT [DEFAULT 1, CHECK > 0],
    condition_on_checkout: ENUM('GOOD', 'FAIR', 'DAMAGED') 
        [DEFAULT 'GOOD'],
    condition_on_return: ENUM('GOOD', 'FAIR', 'DAMAGED', 'NOT_RETURNED') 
        [DEFAULT 'NOT_RETURNED'],
    damage_notes: TEXT,
    FOREIGN KEY (borrow_id) REFERENCES BORROW_RECORDS(borrow_id) 
        ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES EQUIPMENT(equipment_id) 
        ON DELETE RESTRICT
)
```

---

## Indexes for Performance

```sql
CREATE INDEX idx_users_role ON USERS(role);
CREATE INDEX idx_users_status ON USERS(status);
CREATE INDEX idx_equipment_barcode ON EQUIPMENT(barcode);
CREATE INDEX idx_equipment_status ON EQUIPMENT(status);
CREATE INDEX idx_borrow_borrower ON BORROW_RECORDS(borrower_id);
CREATE INDEX idx_borrow_custodian ON BORROW_RECORDS(custodian_id);
CREATE INDEX idx_borrow_status ON BORROW_RECORDS(return_status);
CREATE INDEX idx_borrow_dates ON BORROW_RECORDS(borrow_date, actual_return_date);
CREATE INDEX idx_class_enrollment_student ON CLASS_ENROLLMENT(student_id);
CREATE INDEX idx_events_date ON EVENTS(event_date);
```

---

## Data Integrity Rules

### Referential Integrity
- All foreign keys enforce referential integrity
- ON DELETE CASCADE used for detail/child records
- ON DELETE RESTRICT prevents deletion of parent records with dependent data
- ON DELETE SET NULL allows null values when parent is deleted (optional relationships)

### Domain Integrity
- Data types enforce domain constraints
- ENUM types restrict values to specific sets
- CHECK constraints validate numeric ranges and formats
- DEFAULT values ensure consistent initial states

### Entity Integrity
- Primary keys prevent duplicate records
- UNIQUE constraints prevent duplicate values in key columns
- NOT NULL constraints ensure required fields are populated

### Business Rule Constraints
- Equipment value must be greater than 0
- Year level must be between 1 and 4
- Email must follow valid format
- Username must be at least 3 characters
- Expected return date must be future date or current date

---

## Normalization

The schema follows Third Normal Form (3NF):
- **First Normal Form (1NF)**: All attributes contain atomic values
- **Second Normal Form (2NF)**: All non-key attributes depend on entire primary key
- **Third Normal Form (3NF)**: No transitive dependencies between non-key attributes
- **Boyce-Codd Normal Form (BCNF)**: Additional normalization in complex relationships

---

## Data Volume Estimates

- **USERS**: 50-100 records (staff and students)
- **STUDENTS**: 30-40 records
- **EQUIPMENT**: 50-150 items
- **LABORATORY_CLASSES**: 5-10 classes per semester
- **CLASS_ENROLLMENT**: 100-200 enrollments per semester
- **BORROW_RECORDS**: 1000+ transactions per semester
- **BORROW_DETAILS**: 2000+ items borrowed per semester

