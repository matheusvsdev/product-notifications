package com.matheusvsdev.product_notifications.dto;

import com.matheusvsdev.product_notifications.domain.Category;
import com.matheusvsdev.product_notifications.domain.Product;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryDTO {

    private Long id;
    private String description;
    private Set<SubCategoryDTO> subcategories = new HashSet<>();

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public CategoryDTO(Category category, Product product) {
        this.id = category.getId();
        this.description = category.getDescription();
        this.subcategories = category.getSubCategories()
                .stream()
                .filter(subCat -> product.getSubcategories().contains(subCat))
                .map(SubCategoryDTO::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Set<SubCategoryDTO> getSubcategories() {
        return subcategories;
    }
}
