package com.matheusvsdev.product_notifications.service;

import com.matheusvsdev.product_notifications.domain.Notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

     // Envia uma notificação sobre o produto clicado.
    protected void sendNotification(String imageUrl, String productName, Double price) {
        Notification notification = new Notification("O preço baixou! ", imageUrl, productName, price);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
