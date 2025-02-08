package com.matheusvsdev.product_notifications.dto;

import com.matheusvsdev.product_notifications.domain.Discount;

import java.time.LocalDate;

public class ResponseDiscountDTO {

    private Long id;
    private Double price;
    private LocalDate startDate;
    private LocalDate endDate;

    public ResponseDiscountDTO() {
    }

    public ResponseDiscountDTO(Long id, Double price, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ResponseDiscountDTO(Discount discount) {
        if (discount != null) {
            this.id = discount.getId();
            this.price = discount.getPrice();
            this.startDate = discount.getStartDate();
            this.endDate = discount.getEndDate();
        }
    }

    public Long getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
