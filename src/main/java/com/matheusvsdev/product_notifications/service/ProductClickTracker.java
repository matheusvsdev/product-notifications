package com.matheusvsdev.product_notifications.service;

import com.matheusvsdev.product_notifications.domain.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ProductClickTracker {

    private Map<Long, Integer> productClickCounts = new HashMap<>();
    private Map<Long, Timer> productTimers = new HashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void productClicked(Long productId, String productName, String imageUrl) {
        productClickCounts.put(productId, productClickCounts.getOrDefault(productId, 0) + 1);

        if (productClickCounts.get(productId) > 2) {
            scheduleNotification(productId, productName, imageUrl);
        }
    }

    private void scheduleNotification(Long productId, String productName, String imageUrl) {
        if (!productTimers.containsKey(productId)) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendNotification(productName, imageUrl);
                    System.out.println("Product clicked " + productName);
                    productTimers.remove(productId); // Remover o timer após envio da notificação
                }
            }, 5 * 1000); // 5 segundos em milissegundos

            productTimers.put(productId, timer);
        }
    }

    private void sendNotification(String productName, String imageUrl) {
        Notification notification = new Notification("O produto que você gostou está te esperando", imageUrl, productName);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
