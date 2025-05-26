package org.example.tpfinal.model;

import java.util.concurrent.CompletableFuture;

public class EmailNotificationService implements NotificationService {
    @Override
    public void envoyerNotification(String message) {
        System.out.println("Email envoyé: " + message);
    }

    @Override
    public CompletableFuture<Void> envoyerNotificationAsync(String message) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000); // Simulation d'envoi
                envoyerNotification(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
