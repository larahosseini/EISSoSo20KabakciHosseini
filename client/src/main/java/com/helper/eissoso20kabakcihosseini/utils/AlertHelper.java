package com.helper.eissoso20kabakcihosseini.utils;

import com.helper.eissoso20kabakcihosseini.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class AlertHelper {

    public static Alert alert = new Alert(Alert.AlertType.NONE);
    public static Stage stage = new Stage();

    public static void showErrorAlert(String error) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Error");
        alert.setHeaderText("Es ist ein Fehler aufgetreten");
        alert.setContentText("Fehler: " + error);
        alert.showAndWait();
    }

    public static void showSuccesAlert(String headerText, String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Success");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showLoadingDialog() {
        try {
            Parent dialog = App.loadFXML("loading");
            stage.setScene(new Scene(dialog));
            stage.initOwner(App.window);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showWarningBeforeDeleteAlert(String email, String id) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Warnung");
        alert.setHeaderText("Account löschen");
        alert.setContentText("Bitte bestätigen Sie die Löschung Ihres Accounts");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK && SessionHandler.deleteSession() && Data.deleteUserData(email)) {
            Task<Void> task = APICaller.deleteAccount(id);
            task.messageProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    AlertHelper.showSuccesAlert("Löschung durchgeführt", "Dein Account wurde erfolgreich gelöscht.");
                    try {
                        App.setRoot("login");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            new Thread(task).start();
        }
    }
}
