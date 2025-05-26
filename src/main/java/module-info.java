module org.example.tpfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.xml.bind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // Ouvre le package principal à JavaFX FXML (pas seulement model)
    opens org.example.tpfinal to javafx.fxml;

    // Ouvre aussi le package model si vous avez des contrôleurs là-dedans
    opens org.example.tpfinal.model to javafx.fxml;

    // Ouvre le package controller si vous créez des contrôleurs séparés
    opens org.example.tpfinal.controller to javafx.fxml;

    // Exporte le package principal
    exports org.example.tpfinal;

    exports org.example.tpfinal.model;
    exports org.example.tpfinal.controller;
}