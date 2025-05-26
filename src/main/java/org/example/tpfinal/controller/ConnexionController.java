package org.example.tpfinal.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.tpfinal.model.Participant;
import org.example.tpfinal.utils.PersistenceServicePart;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
public class ConnexionController implements Initializable {
    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private Button connecterButton;
    @FXML private Button annulerButton;

    private Participant participant;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    private void sauvegarderParticipants(List<Participant> participants) {
        try {
            // Créer le dossier data s'il n'existe pas
            new File("data").mkdirs();

            // Utiliser le service de persistance pour participants
            PersistenceServicePart participantService = new PersistenceServicePart();

            // Sauvegarde synchrone
            participantService.sauvegarderParticipants(participants, "data/participants.json");
            System.out.println("Participants sauvegardés avec succès dans data/participants.json");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert( "Erreur de sauvegarde",
                    "Impossible de sauvegarder les participants : " + e.getMessage());
        }
    }

    @FXML
    private void handleConnecter() {
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();


        if (nom.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        participant = new Participant(UUID.randomUUID().toString(), nom, email);
        sauvegarderParticipants((List<Participant>) participant);
        closeWindow();
    }

    @FXML
    private void handleAnnuler() {
        participant = null;
        closeWindow();
    }

    private void closeWindow() {
        if (stage == null) {
            stage = (Stage) connecterButton.getScene().getWindow();
        }
        stage.close();
    }

    public Participant getParticipant() {
        return participant;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

