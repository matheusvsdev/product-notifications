package com.matheusvsdev.product_notifications.domain;

public class Notification {

    private String message;
    private String imageUrl;
    private String productName;
    private Double price;

    public Notification() {
    }

    public Notification(String message, String imageUrl, String productName, Double price) {
        this.message = message;
        this.imageUrl = imageUrl;
        this.productName = productName;
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
