package org.example.tpfinal.model;

import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    void envoyerNotification(String message);
    CompletableFuture<Void> envoyerNotificationAsync(String message);
}
