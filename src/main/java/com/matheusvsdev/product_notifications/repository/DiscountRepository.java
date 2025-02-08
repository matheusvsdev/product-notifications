package com.matheusvsdev.product_notifications.repository;

import com.matheusvsdev.product_notifications.domain.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
