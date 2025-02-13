package com.matheusvsdev.product_notifications.projections;

public interface UserDetailsProjection {

    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthorities();
}
