package org.example.tpfinal.model;



import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Conference.class, name = "conference"),
        @JsonSubTypes.Type(value = Concert.class, name = "concert")
})
public abstract class Evenement {
    protected String id;
    protected String nom;
    protected LocalDateTime date;
    protected String lieu;
    protected int capaciteMax;
    protected List<Participant> participants;
    protected List<EvenementObserver> observers;

    public Evenement() {
        this.participants = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public Evenement(String id, String nom, LocalDateTime date, String lieu, int capaciteMax) {
        this();
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
    }

    public void ajouterParticipant(Participant participant) throws CapaciteMaxAtteinteException {
        if (participants.size() >= capaciteMax) {
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte pour l'événement: " + nom);
        }
        if (!participants.contains(participant)) {
            participants.add(participant);
            notifierObservateurs("Nouveau participant ajouté: " + participant.getNom());
        }
    }

    public void retirerParticipant(Participant participant) {
        if (participants.remove(participant)) {
            notifierObservateurs("Participant retiré: " + participant.getNom());
        }
    }

    public void annuler() {
        notifierObservateurs("Événement ANNULÉ: " + nom + " prévu le " + date);
    }

    public void modifier() {
        notifierObservateurs("Événement MODIFIÉ: " + nom);
    }

    public void ajouterObserver(EvenementObserver observer) {
        observers.add(observer);
    }

    public void retirerObserver(EvenementObserver observer) {
        observers.remove(observer);
    }

    private void notifierObservateurs(String message) {
        for (EvenementObserver observer : observers) {
            observer.notifier(message);
        }
    }

    public abstract String afficherDetails();


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public int getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }
    public List<Participant> getParticipants() { return participants; }
    public void setParticipants(List<Participant> participants) { this.participants = participants; }
}
