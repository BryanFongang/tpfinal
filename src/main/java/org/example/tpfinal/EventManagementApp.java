package org.example.tpfinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class EventManagementApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        primaryStage.setTitle("Système de Gestion d'Événements");
        primaryStage.setScene(new Scene(root, 900, 758));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}