package com.matheusvsdev.product_notifications.domain;

public class Notification {

    private String message;
    private String imageUrl;
    private String productName;

    public Notification() {
    }

    public Notification(String message, String imageUrl, String productName) {
        this.message = message;
        this.imageUrl = imageUrl;
        this.productName = productName;
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
}
