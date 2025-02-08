package com.matheusvsdev.product_notifications.dto;

import com.matheusvsdev.product_notifications.domain.Category;

public class CategoryDTO {

    private Long id;
    private String description;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.description = category.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
