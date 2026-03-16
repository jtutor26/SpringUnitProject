package com.example.unitprojectspring.DTO;

public class TaskDTO {
    private Long id;
    private Long sectionId;
    private String title;
    private String description;
    private String cargoClass;
    private String deliveryDate;
    private boolean isCompleted;

    // Constructors
    public TaskDTO() {}

    public TaskDTO(Long id, String title, String description, String cargoClass, String deliveryDate, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cargoClass = cargoClass;
        this.deliveryDate = deliveryDate;
        this.isCompleted = isCompleted;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCargoClass() { return cargoClass; }
    public void setCargoClass(String cargoClass) { this.cargoClass = cargoClass; }

    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}