package org.example.tpfinal.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.tpfinal.model.Concert;
import org.example.tpfinal.model.Evenement;
import org.example.tpfinal.model.GestionEvenements;
import org.example.tpfinal.utils.PersistenceService;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class AjouterConcertController implements Initializable {
    @FXML private TextField nomField;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField;
    @FXML private TextField lieuField;
    @FXML private TextField capaciteField;
    @FXML private TextField artisteField;
    @FXML private TextField genreField;
    @FXML private Button ajouterButton;
    @FXML private Button annulerButton;

    private GestionEvenements gestionEvenements;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionEvenements = GestionEvenements.getInstance();
    }

    @FXML
    private void handleAjouter() {
        try {

            if (!validerChamps()) {
                return;
            }

            String nom = nomField.getText().trim();
            String lieu = lieuField.getText().trim();
            String artiste = artisteField.getText().trim();
            String genre = genreField.getText().trim();
            int capacite = Integer.parseInt(capaciteField.getText().trim());

            LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(),
                    java.time.LocalTime.parse(heureField.getText().trim()));

            Concert concert = new Concert(
                    UUID.randomUUID().toString(),
                    nom, dateTime, lieu, capacite, artiste, genre
            );

            gestionEvenements.ajouterEvenement(concert);

            sauvegarderEvenements();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Concert ajouté avec succès!");
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La capacité doit être un nombre entier valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    private boolean validerChamps() {
        // ici on verifie les champs obligatoires
        if (nomField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Le nom du concert est obligatoire.");
            return false;
        }

        if (datePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "La date est obligatoire.");
            return false;
        }

        if (heureField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "L'heure est obligatoire.");
            return false;
        }

        if (lieuField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Le lieu est obligatoire.");
            return false;
        }

        if (capaciteField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "La capacité est obligatoire.");
            return false;
        }

        if (artisteField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "L'artiste est obligatoire.");
            return false;
        }

        if (genreField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Le genre musical est obligatoire.");
            return false;
        }

        // Validation du format de l'heure
        try {
            java.time.LocalTime.parse(heureField.getText().trim());
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Validation",
                    "Format d'heure invalide. Utilisez le format HH:MM (ex: 14:30).");
            return false;
        }

        // Validation de la capacité
        try {
            int capacite = Integer.parseInt(capaciteField.getText().trim());
            if (capacite <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation", "La capacité doit être supérieure à 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", "La capacité doit être un nombre entier valide.");
            return false;
        }

        return true;
    }

    private void sauvegarderEvenements() {
        try {
            // Créer le dossier data s'il n'existe pas
            new File("data").mkdirs();

            PersistenceService persistenceService = new PersistenceService();
            List<Evenement> listeEvenements = new ArrayList<>(gestionEvenements.obtenirTousEvenements());


            persistenceService.sauvegarderJSON(listeEvenements, "data/evenements.json");
            System.out.println("Événements sauvegardés avec succès dans data/evenements.json");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de sauvegarde",
                    "Impossible de sauvegarder les événements: " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) ajouterButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}