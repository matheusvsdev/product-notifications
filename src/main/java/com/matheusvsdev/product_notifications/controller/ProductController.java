package com.matheusvsdev.product_notifications.controller;

import com.matheusvsdev.product_notifications.dto.ResponseProductDTO;
import com.matheusvsdev.product_notifications.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ResponseProductDTO>> getAllProducts(Pageable pageable) {
        Page<ResponseProductDTO> products = productService.findAll(pageable);
        return ResponseEntity.ok().body(products);
    }
}
