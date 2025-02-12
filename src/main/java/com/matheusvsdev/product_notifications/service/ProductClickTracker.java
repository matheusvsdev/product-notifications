package com.matheusvsdev.product_notifications.service;

import com.matheusvsdev.product_notifications.domain.Notification;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductClickTracker {

    private final CacheManager cacheManager;
    private final TaskScheduler taskScheduler;
    private final SimpMessagingTemplate messagingTemplate;

    public ProductClickTracker(CacheManager cacheManager, TaskScheduler taskScheduler, SimpMessagingTemplate messagingTemplate) {
        this.cacheManager = cacheManager;
        this.taskScheduler = taskScheduler;
        this.messagingTemplate = messagingTemplate;
    }
    /*
     * Metodo chamado quando um produto é clicado.
     * Incrementa o contador de cliques no cache e agenda uma notificação se o produto for clicado mais de duas vezes.
     */
    public void productClicked(Long productId, String productName, String imageUrl) {
        // Obter o contador de cliques do cache, ou inicializar se não existir
        Cache.ValueWrapper cacheValueWrapper = Objects.requireNonNull(cacheManager.getCache("productClicks")).get(productId);
        AtomicInteger clickCount = Optional.ofNullable(cacheValueWrapper != null ? (AtomicInteger) cacheValueWrapper.get() : null)
                .orElse(new AtomicInteger(0));

        // Incrementar o contador de clicks
        clickCount.incrementAndGet();
        Objects.requireNonNull(cacheManager.getCache("productClicks")).put(productId, clickCount);

        // Se o produto for clicado mais de duas vezes, agendar uma notificação
        if (clickCount.get() > 2) {
            // Agendar a notificação para 5 segundos no futuro
            Instant startTime = Instant.now().plus(Duration.ofSeconds(5));
            taskScheduler.schedule(() -> {
                sendNotification(productName, imageUrl);
                // Zerar o contador de cliques no cache após o envio da notificação
                Objects.requireNonNull(cacheManager.getCache("productClicks")).put(productId, new AtomicInteger(0));
            }, startTime);
        }
    }
     // Envia uma notificação sobre o produto clicado.
    private void sendNotification(String productName, String imageUrl) {
        Notification notification = new Notification("Psiu! Não deixe de levar ", imageUrl, productName);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
