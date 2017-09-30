package com.utils;

import javafx.scene.control.Alert;


public class Alerts {
    public static void errorAlert(String message, String appTitle) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(appTitle);
        alert.setContentText(message);
        alert.showAndWait();

    }

    public static void informationAlert(String message, String appTitle) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(appTitle);
        alert.setContentText(message);
        alert.showAndWait();

    }
}
