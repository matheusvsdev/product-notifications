package com.matheusvsdev.product_notifications.dto;

import com.matheusvsdev.product_notifications.domain.SubCategory;

public class SubCategoryDTO {

    private Long id;
    private String description;

    public SubCategoryDTO() {
    }

    public SubCategoryDTO(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public SubCategoryDTO(SubCategory subCategory) {
        this.id = subCategory.getId();
        this.description = subCategory.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
