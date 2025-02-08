package com.matheusvsdev.product_notifications.repository;

import com.matheusvsdev.product_notifications.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
