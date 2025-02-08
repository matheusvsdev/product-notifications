package com.matheusvsdev.product_notifications.dto;

import com.matheusvsdev.product_notifications.domain.User;

public class CreateUserDTO {

    private String fullName;
    private String email;
    private String password;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public CreateUserDTO(User user) {
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
