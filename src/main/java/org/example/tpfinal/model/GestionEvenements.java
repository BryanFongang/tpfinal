package org.example.tpfinal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GestionEvenements {
    private static GestionEvenements instance;
    private Map<String, Evenement> evenements;
    private NotificationService notificationService;

    private GestionEvenements() {
        this.evenements = new HashMap<>();
        this.notificationService = new EmailNotificationService();
    }

    public static synchronized GestionEvenements getInstance() {
        if (instance == null) {
            instance = new GestionEvenements();
        }
        return instance;
    }

    public void ajouterEvenement(Evenement evenement) throws EvenementDejaExistantException {
        if (evenements.containsKey(evenement.getId())) {
            throw new EvenementDejaExistantException("Événement avec l'ID " + evenement.getId() + " existe déjà");
        }
        evenements.put(evenement.getId(), evenement);
    }

    public void supprimerEvenement(String id) {
        Evenement evenement = evenements.remove(id);
        if (evenement != null) {
            evenement.annuler();
        }
    }

    public Evenement rechercherEvenement(String id) {
        return evenements.get(id);
    }

    public List<Evenement> obtenirTousEvenements() {
        return new ArrayList<>(evenements.values());
    }

    public List<Evenement> rechercherParNom(String nom) {
        return evenements.values().stream()
                .filter(e -> e.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Evenement> rechercherParType(Class<? extends Evenement> type) {
        return evenements.values().stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }
}
