package org.example.tpfinal.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Conference extends Evenement {
    private String theme;
    private List<String> intervenants;

    public Conference() {
        super();
        this.intervenants = new ArrayList<>();
    }

    public Conference(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                      String theme, List<String> intervenants) {
        super(id, nom, date, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = intervenants != null ? intervenants : new ArrayList<>();
    }

    @Override
    public String afficherDetails() {
        return String.format("Conférence: %s\nThème: %s\nDate: %s\nLieu: %s\nCapacité: %d/%d\nIntervenants: %s",
                nom, theme, date, lieu, participants.size(), capaciteMax,
                String.join(", ", intervenants));
    }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public List<String> getIntervenants() { return intervenants; }
    public void setIntervenants(List<String> intervenants) { this.intervenants = intervenants; }
}