package com.matheusvsdev.product_notifications.service;

import com.matheusvsdev.product_notifications.domain.Product;

import com.matheusvsdev.product_notifications.dto.ResponseProductDTO;
import com.matheusvsdev.product_notifications.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Transactional(readOnly = true)
    public Page<ResponseProductDTO> findAll(Pageable pageable) {
        long startTime = System.currentTimeMillis();
        Page<Product> products = productRepository.findAll(pageable);
        long endTime = System.currentTimeMillis();

        logger.info("Time taken to find all products in {} ms", endTime - startTime);

        return products.map(ResponseProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ResponseProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ResponseProductDTO(product);
    }
}
