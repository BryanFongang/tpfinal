package org.example.tpfinal.utils;
import org.example.tpfinal.model.Concert;
import org.example.tpfinal.model.Conference;
import org.example.tpfinal.model.Evenement;


import java.io.IOException;
import java.util.List;

import org.example.tpfinal.model.Participant;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersistenceService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public PersistenceService() {
        // Aucune initialisation nécessaire
    }


    public void sauvegarderJSON(List<Evenement> evenements, String fichier) throws IOException {
        try (FileWriter writer = new FileWriter(fichier, java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write(convertirListeEvenementsVersJSON(evenements));
        }
    }


    public List<Evenement> chargerJSON(String fichier) throws IOException {
        StringBuilder contenu = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fichier), java.nio.charset.StandardCharsets.UTF_8))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                contenu.append(ligne).append("\n");
            }
        }
        return parseJSONVersListeEvenements(contenu.toString());
    }

    private String convertirListeEvenementsVersJSON(List<Evenement> evenements) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < evenements.size(); i++) {
            json.append("  ").append(convertirEvenementVersJSON(evenements.get(i)));
            if (i < evenements.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("]");
        return json.toString();
    }

    private String convertirEvenementVersJSON(Evenement evenement) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");

        // Propriétés communes
        json.append("    \"type\": \"").append(evenement.getClass().getSimpleName()).append("\",\n");
        json.append("    \"id\": \"").append(escaperJSON(evenement.getId())).append("\",\n");
        json.append("    \"nom\": \"").append(escaperJSON(evenement.getNom())).append("\",\n");
        json.append("    \"date\": \"").append(evenement.getDate().format(DATE_FORMATTER)).append("\",\n");
        json.append("    \"lieu\": \"").append(escaperJSON(evenement.getLieu())).append("\",\n");
        json.append("    \"capaciteMax\": ").append(evenement.getCapaciteMax()).append(",\n");

        // Participants
        json.append("    \"participants\": [\n");
        List<Participant> participants = evenement.getParticipants();
        for (int i = 0; i < participants.size(); i++) {
            json.append("      ").append(convertirParticipantVersJSON(participants.get(i)));
            if (i < participants.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("    ]");

        // Propriétés spécifiques selon le type
        if (evenement instanceof Conference) {
            Conference conf = (Conference) evenement;
            json.append(",\n");
            json.append("    \"theme\": \"").append(escaperJSON(conf.getTheme())).append("\",\n");
            json.append("    \"intervenants\": [");
            List<String> intervenants = conf.getIntervenants();
            for (int i = 0; i < intervenants.size(); i++) {
                json.append("\"").append(escaperJSON(intervenants.get(i))).append("\"");
                if (i < intervenants.size() - 1) {
                    json.append(", ");
                }
            }
            json.append("]\n");
        } else if (evenement instanceof Concert) {
            Concert concert = (Concert) evenement;
            json.append(",\n");
            json.append("    \"artiste\": \"").append(escaperJSON(concert.getArtiste())).append("\",\n");
            json.append("    \"genreMusical\": \"").append(escaperJSON(concert.getGenreMusical())).append("\"\n");
        } else {
            json.append("\n");
        }

        json.append("  }");
        return json.toString();
    }

    private String convertirParticipantVersJSON(Participant participant) {
        return String.format("{\n" +
                        "        \"id\": \"%s\",\n" +
                        "        \"nom\": \"%s\",\n" +
                        "        \"email\": \"%s\"\n" +
                        "      }",
                escaperJSON(participant.getId()),
                escaperJSON(participant.getNom()),
                escaperJSON(participant.getEmail()));
    }

    private String escaperJSON(String valeur) {
        if (valeur == null) return "";
        return valeur.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private List<Evenement> parseJSONVersListeEvenements(String json) {
        List<Evenement> evenements = new ArrayList<>();

        // Enlever les crochets principaux et diviser par objets
        json = json.trim();
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

        // Parser chaque objet événement
        List<String> objetsJSON = diviserObjetsJSON(json);

        for (String objetJSON : objetsJSON) {
            try {
                Evenement evenement = parseObjetEvenement(objetJSON.trim());
                if (evenement != null) {
                    evenements.add(evenement);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du parsing d'un événement: " + e.getMessage());
            }
        }

        return evenements;
    }

    private List<String> diviserObjetsJSON(String json) {
        List<String> objets = new ArrayList<>();
        int niveau = 0;
        int debut = 0;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (niveau == 0) debut = i;
                niveau++;
            } else if (c == '}') {
                niveau--;
                if (niveau == 0) {
                    objets.add(json.substring(debut, i + 1));
                }
            }
        }

        return objets;
    }

    private Evenement parseObjetEvenement(String objetJSON) {
        try {
            String type = extraireValeurString(objetJSON, "type");
            String id = extraireValeurString(objetJSON, "id");
            String nom = extraireValeurString(objetJSON, "nom");
            String dateStr = extraireValeurString(objetJSON, "date");
            String lieu = extraireValeurString(objetJSON, "lieu");
            int capaciteMax = extraireValeurInt(objetJSON, "capaciteMax");

            LocalDateTime date = LocalDateTime.parse(dateStr, DATE_FORMATTER);
            List<Participant> participants = parseParticipants(objetJSON);

            Evenement evenement;

            if ("Conference".equals(type)) {
                String theme = extraireValeurString(objetJSON, "theme");
                List<String> intervenants = parseIntervenants(objetJSON);
                evenement = new Conference(id, nom, date, lieu, capaciteMax, theme, intervenants);
            } else if ("Concert".equals(type)) {
                String artiste = extraireValeurString(objetJSON, "artiste");
                String genreMusical = extraireValeurString(objetJSON, "genreMusical");
                evenement = new Concert(id, nom, date, lieu, capaciteMax, artiste, genreMusical);
            } else {
                return null; // Type non supporté
            }

            // Ajouter les participants
            for (Participant participant : participants) {
                try {
                    evenement.ajouterParticipant(participant);
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'ajout du participant: " + e.getMessage());
                }
            }

            return evenement;

        } catch (Exception e) {
            System.err.println("Erreur lors du parsing de l'événement: " + e.getMessage());
            return null;
        }
    }

    private String extraireValeurString(String json, String cle) {
        Pattern pattern = Pattern.compile("\"" + cle + "\"\\s*:\\s*\"([^\"]*?)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).replace("\\\"", "\"")
                    .replace("\\\\", "\\")
                    .replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\t", "\t");
        }
        return "";
    }

    private int extraireValeurInt(String json, String cle) {
        Pattern pattern = Pattern.compile("\"" + cle + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private List<Participant> parseParticipants(String json) {
        List<Participant> participants = new ArrayList<>();

        // Extraire la section participants
        Pattern pattern = Pattern.compile("\"participants\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String participantsJSON = matcher.group(1);
            List<String> objetsParticipants = diviserObjetsJSON(participantsJSON);

            for (String objetParticipant : objetsParticipants) {
                String id = extraireValeurString(objetParticipant, "id");
                String nom = extraireValeurString(objetParticipant, "nom");
                String email = extraireValeurString(objetParticipant, "email");
                participants.add(new Participant(id, nom, email));
            }
        }

        return participants;
    }

    private List<String> parseIntervenants(String json) {
        List<String> intervenants = new ArrayList<>();

        Pattern pattern = Pattern.compile("\"intervenants\"\\s*:\\s*\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String intervenantsStr = matcher.group(1);
            Pattern intervPattern = Pattern.compile("\"([^\"]+)\"");
            Matcher intervMatcher = intervPattern.matcher(intervenantsStr);

            while (intervMatcher.find()) {
                intervenants.add(intervMatcher.group(1));
            }
        }

        return intervenants;
    }

  }