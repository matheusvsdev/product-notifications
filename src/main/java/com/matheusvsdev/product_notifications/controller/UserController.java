package com.matheusvsdev.product_notifications.controller;

import com.matheusvsdev.product_notifications.dto.CreateUserDTO;
import com.matheusvsdev.product_notifications.dto.ResponseUserDTO;
import com.matheusvsdev.product_notifications.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseUserDTO> insertUser(@RequestBody CreateUserDTO userDTO) {
        ResponseUserDTO responseUserDTO = userService.insertUser(userDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseUserDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(responseUserDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseUserDTO>> getAllUsers(Pageable pageable) {
        Page<ResponseUserDTO> list = userService.findAll(pageable);
        return ResponseEntity.ok().body(list);
    }
}
