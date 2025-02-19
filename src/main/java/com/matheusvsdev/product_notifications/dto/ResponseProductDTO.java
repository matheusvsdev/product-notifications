package com.matheusvsdev.product_notifications.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.matheusvsdev.product_notifications.domain.Product;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProductDTO {

    private Long id;
    private String img_url;
    private String title;
    private String description;
    private Double price;
    private DiscountDTO discount;
    private Set<CategoryDTO> categories = new HashSet<>();

    public ResponseProductDTO() {
    }

    public ResponseProductDTO(Long id,
                              String img_url,
                              String title,
                              String description,
                              Double price,
                              DiscountDTO discount) {
        this.id = id;
        this.img_url = img_url;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discount = discount;
    }

    public ResponseProductDTO(Product product) {
        this.id = product.getId();
        this.img_url = product.getImgUrl();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();

        if (product.getDiscount() != null) {
            this.discount = new DiscountDTO(product.getDiscount());
        } else {
            this.discount = null;
        }

        this.categories = product.getCategories()
                .stream()
                .map(category -> new CategoryDTO(category, product))
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public DiscountDTO getDiscount() {
        return discount;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }
}
