package models;

import java.time.LocalDateTime;

/**
 * Equipment Model - Represents an equipment item in the system
 */
public class Equipment {
    private int equipmentId;
    private String barcode;
    private String equipmentName;
    private int categoryId;
    private String categoryName;
    private String description;
    private String status; // AVAILABLE, CHECKED_OUT, MAINTENANCE, RETIRED
    private String purchaseDate;
    private double value;
    private String location;
    private LocalDateTime createdDate;

    // Constructors
    public Equipment() {}

    public Equipment(int equipmentId, String barcode, String equipmentName,
                     String categoryName, String status, String location) {
        this.equipmentId = equipmentId;
        this.barcode = barcode;
        this.equipmentName = equipmentName;
        this.categoryName = categoryName;
        this.status = status;
        this.location = location;
    }

    public Equipment(String barcode, String equipmentName, int categoryId,
                     String description, double value, String location) {
        this.barcode = barcode;
        this.equipmentName = equipmentName;
        this.categoryId = categoryId;
        this.description = description;
        this.value = value;
        this.location = location;
        this.status = "AVAILABLE";
    }

    // Getters and Setters
    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getEquipmentName() { return equipmentName; }
    public void setEquipmentName(String equipmentName) { this.equipmentName = equipmentName; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentId=" + equipmentId +
                ", barcode='" + barcode + '\'' +
                ", equipmentName='" + equipmentName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

