package com.matheusvsdev.product_notifications.dto;

import com.matheusvsdev.product_notifications.domain.User;

public class ResponseUserDTO {

    private Long id;
    private String fullName;
    private String email;

    public ResponseUserDTO() {
    }

    public ResponseUserDTO(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public ResponseUserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
