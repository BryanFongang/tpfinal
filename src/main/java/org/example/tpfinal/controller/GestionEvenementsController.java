package org.example.tpfinal.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.tpfinal.model.Conference;
import org.example.tpfinal.model.Evenement;
import org.example.tpfinal.model.EvenementDejaExistantException;
import org.example.tpfinal.model.GestionEvenements;
import org.example.tpfinal.utils.PersistenceService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class GestionEvenementsController implements Initializable {
    @FXML private TableView<Evenement> evenementsTable;
    @FXML private TableColumn<Evenement, String> nomColumn;
    @FXML private TableColumn<Evenement, String> typeColumn;
    @FXML private TableColumn<Evenement, String> dateColumn;
    @FXML private TableColumn<Evenement, String> lieuColumn;
    @FXML private TableColumn<Evenement, Integer> capaciteColumn;

    @FXML private Button ajouterConferenceButton;
    @FXML private Button ajouterConcertButton;
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;
    @FXML private Button rafraichirButton;

    private GestionEvenements gestionEvenements;
    private ObservableList<Evenement> evenementsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionEvenements = GestionEvenements.getInstance();
        evenementsList = FXCollections.observableArrayList();
        chargerDonneesExistantes();
        configureTable();
        refreshTable();
    }

    private void configureTable() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typeColumn.setCellValueFactory(cellData -> {
            String type = cellData.getValue() instanceof Conference ? "Conférence" : "Concert";
            return new javafx.beans.property.SimpleStringProperty(type);
        });
        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDate();
            String dateStr = date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));

        evenementsTable.setItems(evenementsList);
    }
    private void chargerDonneesExistantes() {
        try {
            PersistenceService persistenceService = new PersistenceService();
            List<Evenement> evenements = persistenceService.chargerJSON("data/evenements.json");

            // Ajouter les événements chargés au gestionnaire
            for (Evenement evenement : evenements) {
                gestionEvenements.ajouterEvenement(evenement);
            }

            System.out.println("Événements chargés avec succès");
        } catch (IOException e) {
            System.out.println("Aucun fichier d'événements trouvé ou erreur de lecture: " + e.getMessage());
        } catch (EvenementDejaExistantException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void handleAjouterConference() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/tpfinal/ajouterConference.fxml")));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajouter une Conférence");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire de conférence");
        }
    }

    @FXML
    private void handleAjouterConcert() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/tpfinal/ajouterConcert.fxml")));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajouter un Concert");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire de concert");
        }
    }

    @FXML
    private void handleSupprimer() {
        Evenement selected = evenementsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Aucune sélection", "Veuillez sélectionner un événement à supprimer");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer l'événement");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            gestionEvenements.supprimerEvenement(selected.getId());
            refreshTable();
        }
    }

    @FXML
    private void handleRafraichir() {
        refreshTable();
    }
    @FXML
    private void handleModifier() {
        refreshTable();
    }
    private void refreshTable() {
        evenementsList.clear();
        evenementsList.addAll(gestionEvenements.obtenirTousEvenements());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

