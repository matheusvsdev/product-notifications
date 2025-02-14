package com.matheusvsdev.product_notifications.service;

import com.matheusvsdev.product_notifications.domain.Discount;
import com.matheusvsdev.product_notifications.domain.Product;
import com.matheusvsdev.product_notifications.dto.DiscountDTO;
import com.matheusvsdev.product_notifications.dto.ResponseProductDTO;
import com.matheusvsdev.product_notifications.repository.DiscountRepository;
import com.matheusvsdev.product_notifications.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public ResponseProductDTO insertDiscount(Long id, DiscountDTO dto) {
        Product product = productRepository.getReferenceById(id);
        product.setDiscount(applyDiscount(dto));

        productRepository.save(product);

        notificationService.sendNotification(
                product.getImgUrl(),
                product.getTitle(),
                product.getDiscount().getPrice());

        return new ResponseProductDTO(product);
    }

    private Discount applyDiscount(DiscountDTO dto) {
        Discount discount = new Discount();
        discount.setPrice(dto.getPrice());
        discount.setStartDate(dto.getStartDate());
        discount.setEndDate(dto.getEndDate());

        discountRepository.save(discount);

        return discount;
    }
}
