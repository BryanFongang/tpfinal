package org.example.tpfinal.utils;

import org.example.tpfinal.model.Participant;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersistenceServicePart {

    public void sauvegarderParticipants(List<Participant> participants, String fichier) throws IOException {
        try (FileWriter writer = new FileWriter(fichier, StandardCharsets.UTF_8)) {
            writer.write(convertirListeParticipantsVersJSON(participants));
        }
    }

    public List<Participant> chargerParticipants(String fichier) throws IOException {
        File f = new File(fichier);
        if (!f.exists()) return new ArrayList<>();

        StringBuilder contenu = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                contenu.append(ligne).append("\n");
            }
        }

        return parseListeParticipantsJSON(contenu.toString());
    }

    private String convertirListeParticipantsVersJSON(List<Participant> participants) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        for (int i = 0; i < participants.size(); i++) {
            Participant p = participants.get(i);
            json.append("  {\n");
            json.append("    \"id\": \"").append(escape(p.getId())).append("\",\n");
            json.append("    \"nom\": \"").append(escape(p.getNom())).append("\",\n");
            json.append("    \"email\": \"").append(escape(p.getEmail())).append("\"\n");
            json.append("  }");
            if (i < participants.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");
        return json.toString();
    }

    private List<Participant> parseListeParticipantsJSON(String json) {
        List<Participant> participants = new ArrayList<>();
        json = json.trim();
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

        String[] objets = json.split("\\},\\s*\\{");
        for (String objet : objets) {
            objet = objet.trim();
            if (!objet.startsWith("{")) objet = "{" + objet;
            if (!objet.endsWith("}")) objet = objet + "}";

            String id = extraireValeur(objet, "id");
            String nom = extraireValeur(objet, "nom");
            String email = extraireValeur(objet, "email");
            participants.add(new Participant(id, nom, email));
        }

        return participants;
    }

    private String extraireValeur(String json, String cle) {
        Pattern pattern = Pattern.compile("\"" + cle + "\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1).replace("\\\"", "\"").replace("\\\\", "\\") : "";
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
