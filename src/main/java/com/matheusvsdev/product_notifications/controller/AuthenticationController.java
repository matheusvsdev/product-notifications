package com.matheusvsdev.product_notifications.controller;

import com.matheusvsdev.product_notifications.service.AuthService;
import com.matheusvsdev.product_notifications.dto.AuthenticationDTO;
import com.matheusvsdev.product_notifications.dto.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
        return ResponseEntity.ok().body(authService.login(data));
    }
}
