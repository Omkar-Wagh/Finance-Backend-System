package com.finance.dto;
import jakarta.validation.constraints.*;


public class RecordRequestDto {
    private Long id;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Type is required")
    private String type;

    @NotBlank(message = "Category is required")
    @Size(min = 2, max = 30, message = "Category must be between 2-30 characters")
    private String category;

    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;

    @NotNull(message = "Owner userId is required")
    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull(message = "Amount is required") @Positive(message = "Amount must be greater than 0") Double getAmount() {
        return amount;
    }

    public void setAmount(@NotNull(message = "Amount is required") @Positive(message = "Amount must be greater than 0") Double amount) {
        this.amount = amount;
    }

    public @NotNull(message = "Type is required") String getType() {
        return type;
    }

    public void setType(@NotNull(message = "Type is required") String type) {
        this.type = type;
    }

    public @NotBlank(message = "Category is required") @Size(min = 2, max = 30, message = "Category must be between 2-30 characters") String getCategory() {
        return category;
    }

    public void setCategory(@NotBlank(message = "Category is required") @Size(min = 2, max = 30, message = "Category must be between 2-30 characters") String category) {
        this.category = category;
    }

    public @Size(max = 255, message = "Notes cannot exceed 255 characters") String getNotes() {
        return notes;
    }

    public void setNotes(@Size(max = 255, message = "Notes cannot exceed 255 characters") String notes) {
        this.notes = notes;
    }

    public @NotNull(message = "Owner userId is required") Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(@NotNull(message = "Owner userId is required") Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setId(Long id) {
        this.id = id;
    }
}