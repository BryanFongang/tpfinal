package org.example.tpfinal.utils;

import org.example.tpfinal.model.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NotificationServiceA implements NotificationService {
    private final List<String> notifications = new ArrayList<>();

    @Override
    public void envoyerNotification(String message) {
        notifications.add(message);
        System.out.println("[NOTIFICATION] " + message);
    }

    @Override
    public CompletableFuture<Void> envoyerNotificationAsync(String message) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Simulation d'un délai d'envoi
                Thread.sleep(500);
                envoyerNotification(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Notification interrompue", e);
            }
        });
    }

    public CompletableFuture<Void> envoyerNotificationsBatch(List<String> messages) {
        return CompletableFuture.allOf(
                messages.stream()
                        .map(this::envoyerNotificationAsync)
                        .toArray(CompletableFuture[]::new)
        );
    }

    public List<String> obtenirToutesNotifications() {
        return new ArrayList<>(notifications);
    }

    public void viderNotifications() {
        notifications.clear();
    }
}
