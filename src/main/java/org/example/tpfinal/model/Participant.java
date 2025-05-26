package org.example.tpfinal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Participant implements EvenementObserver {
    protected String id;
    protected String nom;
    protected String email;
    private List<String> notifications;

    // ✅ Callback pour mettre à jour l’interface JavaFX
    private transient Consumer<String> notificationCallback;

    public Participant() {
        this.notifications = new ArrayList<>();
    }

    public Participant(String id, String nom, String email) {
        this();
        this.id = id;
        this.nom = nom;
        this.email = email;
    }

    @Override
    public void notifier(String message) {
        notifications.add(message);
        System.out.println("Notification pour " + nom + ": " + message);

        // ✅ Appel du contrôleur via la callback
        if (notificationCallback != null) {
            notificationCallback.accept(message);
        }
    }

    // ✅ Méthode pour que le contrôleur FXML puisse s’abonner
    public void setNotificationCallback(Consumer<String> callback) {
        this.notificationCallback = callback;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getNotifications() { return notifications; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Participant that = (Participant) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
