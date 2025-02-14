package com.matheusvsdev.product_notifications.controller;

import com.matheusvsdev.product_notifications.dto.ResponseProductDTO;
import com.matheusvsdev.product_notifications.service.NotificationService;
import com.matheusvsdev.product_notifications.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private NotificationService productClickTracker;

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping
    public ResponseEntity<Page<ResponseProductDTO>> getAllProducts(Pageable pageable) {
        long startTime = System.currentTimeMillis();
        Page<ResponseProductDTO> products = productService.findAll(pageable);
        long endTime = System.currentTimeMillis();

        logger.info("Time taken to retrieve all products in {} ms", endTime - startTime);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDTO> getProductById(@PathVariable Long id) {
        ResponseProductDTO productDTO = productService.findById(id);
        return ResponseEntity.ok().body(productDTO);
    }
}
