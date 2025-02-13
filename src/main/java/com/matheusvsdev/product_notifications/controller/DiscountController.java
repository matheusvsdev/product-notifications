package com.matheusvsdev.product_notifications.controller;

import com.matheusvsdev.product_notifications.dto.DiscountDTO;
import com.matheusvsdev.product_notifications.dto.ResponseProductDTO;
import com.matheusvsdev.product_notifications.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PutMapping("/{id}")
    public ResponseEntity<ResponseProductDTO> insertDiscount(@PathVariable Long id,
                                                                    @RequestBody DiscountDTO dto) {
        ResponseProductDTO discount = discountService.insertDiscount(id, dto);
        return ResponseEntity.ok().body(discount);
    }
}
