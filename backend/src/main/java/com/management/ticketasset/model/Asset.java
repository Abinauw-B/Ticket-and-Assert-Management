package com.management.ticketasset.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedTo;

    // --- New Fields ---
    private String category;
    private String brand;
    private String model;
    private LocalDate purchaseDate;
    private Double purchasePrice;
    private String vendor;
    private LocalDate warrantyExpiryDate;
    private String physicalLocation;
    private String barcode;
    private String ipAddress;
    private String macAddress;
    private String operatingSystem;
    private String cpuSpecification;
    private String ramSize;
    private String storageSize;
    private String invoiceNumber;
    private LocalDate endOfLifeDate;

    public Asset() {
    }

    public Asset(String name, String serialNumber, String description, AssetStatus status, User assignedTo) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.description = description;
        this.status = status;
        this.assignedTo = assignedTo;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public AssetStatus getStatus() { return status; }
    public void setStatus(AssetStatus status) { this.status = status; }

    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public Double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public LocalDate getWarrantyExpiryDate() { return warrantyExpiryDate; }
    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) { this.warrantyExpiryDate = warrantyExpiryDate; }

    public String getPhysicalLocation() { return physicalLocation; }
    public void setPhysicalLocation(String physicalLocation) { this.physicalLocation = physicalLocation; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }

    public String getOperatingSystem() { return operatingSystem; }
    public void setOperatingSystem(String operatingSystem) { this.operatingSystem = operatingSystem; }

    public String getCpuSpecification() { return cpuSpecification; }
    public void setCpuSpecification(String cpuSpecification) { this.cpuSpecification = cpuSpecification; }

    public String getRamSize() { return ramSize; }
    public void setRamSize(String ramSize) { this.ramSize = ramSize; }

    public String getStorageSize() { return storageSize; }
    public void setStorageSize(String storageSize) { this.storageSize = storageSize; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public LocalDate getEndOfLifeDate() { return endOfLifeDate; }
    public void setEndOfLifeDate(LocalDate endOfLifeDate) { this.endOfLifeDate = endOfLifeDate; }
}
