package com.matheusvsdev.product_notifications.repository;

import com.matheusvsdev.product_notifications.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
