package com.matheusvsdev.product_notifications.dto;

import java.time.Instant;

public record LoginResponseDTO(String token, String typeToken, Instant expiresIn) {
}
