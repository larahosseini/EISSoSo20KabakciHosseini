package com.helper.eissoso20kabakcihosseini.utils;

import com.helper.eissoso20kabakcihosseini.App;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertHelper {

    public static Alert alert = new Alert(Alert.AlertType.NONE);

    public static void showErrorAlert(String error){
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Es ist ein Fehler aufgetreten");
        alert.setContentText("Message: " + error);
        alert.showAndWait();
    }

    public static void showSuccesAlert(String message) {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Der Account wurde erstellt, bitte überprüfe dein Postfach");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showLoadingDialog(){
        try {
            Parent dialog = App.loadFXML("loading");
            Stage stage = new Stage();
            stage.setScene(new Scene(dialog));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
