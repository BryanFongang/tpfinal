package org.example.tpfinal.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.tpfinal.model.Evenement;
import org.example.tpfinal.model.Participant;
import org.example.tpfinal.model.GestionEvenements;
import org.example.tpfinal.utils.NotificationServiceA;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

// Contrôleur principal
public class MainController implements Initializable {
    @FXML private TabPane mainTabPane;
    @FXML private Tab accueilTab;
    @FXML private Tab gestionTab;
    @FXML private Tab participantTab;
    @FXML private TextArea notificationsArea;
    private final NotificationServiceA notificationService = new NotificationServiceA();
    private GestionEvenements gestionEvenements;
    Participant currentParticipant;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionEvenements = GestionEvenements.getInstance();
    }
    public void afficherNotification(String message) {
        Platform.runLater(() -> {
            if (notificationsArea != null) {
                notificationsArea.appendText(message + "\n");
            }
        });
    }


    @FXML
    private void handleConnexion() {
        try {

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/tpfinal/connexion.fxml")));
            Parent root = loader.load();

            if (currentParticipant != null) {
                // ✅ Rediriger les notifications vers la zone FXML
                currentParticipant.setNotificationCallback(this::afficherNotification);

            // ✅ S'inscrire à tous les événements
            for (Evenement e : GestionEvenements.getInstance().obtenirTousEvenements()) {
                e.ajouterObserver(currentParticipant);
            }


            Stage stage = new Stage();
            stage.setTitle("Connexion");
            stage.setScene(new Scene(root));
            stage.setWidth(600);
            stage.setHeight(500);
            stage.setResizable(false);
            stage.show();
;



                mainTabPane.getSelectionModel().select(participantTab);
                notificationService.envoyerNotification("Bienvenue " + currentParticipant.getNom());

                refreshParticipantView();


            } else {
                System.out.println(5);
            }
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de connexion");
        }
    }


    @FXML
    private void handleGestionEvenements() {
        mainTabPane.getSelectionModel().select(gestionTab);
    }

    @FXML
    private void handleAjouterParticipant() {
        if (currentParticipant != null) {
            mainTabPane.getSelectionModel().select(participantTab);
        } else {
            handleConnexion();
        }
    }

    private void refreshParticipantView() {
        // Logique pour rafraîchir la vue participant
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}