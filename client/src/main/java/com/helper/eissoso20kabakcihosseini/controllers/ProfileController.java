package com.helper.eissoso20kabakcihosseini.controllers;

import com.helper.eissoso20kabakcihosseini.App;
import com.helper.eissoso20kabakcihosseini.models.Session;
import com.helper.eissoso20kabakcihosseini.models.User;
import com.helper.eissoso20kabakcihosseini.utils.APICaller;
import com.helper.eissoso20kabakcihosseini.utils.AlertHelper;
import com.helper.eissoso20kabakcihosseini.utils.Data;
import com.helper.eissoso20kabakcihosseini.utils.SessionHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ProfileController implements Initializable {

    @FXML
    private JFXTextField emailField, cityField, streetField, streetNumberField, zipcodeField;

    @FXML
    private JFXButton updateButton, passwordResetButton, deleteAccountButton;

    @FXML
    private JFXToggleButton editButton;

    @FXML
    private Text usernameText;

    @FXML
    private Label emailLabel, streetLabel, zipcodeLabel, cityLabel;

    @FXML
    private VBox labelContainer, textFieldContainer;

    @FXML
    private HBox emailTextContainer;

    private User user;

    @FXML
    private void deleteAccount() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        AlertHelper.showWarningBeforeDeleteAlert(user.getEmail(), user.getId());
    }

    @FXML
    private void updateProfile() {
    }

    @FXML
    private void handleBackButton() {
        try {
            App.setRoot("dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateButton.visibleProperty().bind(editButton.selectedProperty());
        editButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                toggleTextFieldAndLabel(t1, !t1);
            }
        });


        Session session = SessionHandler.getSession();
        if (session == null) {
            App.window.close();
        }

        try {
            user = Data.getUserData(session.getEmail());
            emailLabel.setText(user.getEmail());
            streetLabel.setText(user.getAddress().getStreet() + " " + user.getAddress().getStreetNumber());
            cityLabel.setText(user.getAddress().getCity());
            zipcodeLabel.setText(String.valueOf(user.getAddress().getZipcode()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BooleanBinding booleanBinding = emailField.textProperty().isEmpty()
                .or(cityField.textProperty().isEmpty())
                .or(streetField.textProperty().isEmpty())
                .or(streetNumberField.textProperty().isEmpty())
                .or(zipcodeField.textProperty().isEmpty())
                .or(cityField.textProperty().isEmpty());
        updateButton.disableProperty().bind(booleanBinding);
    }

    private void toggleTextFieldAndLabel(boolean textfieldVisibility, boolean labelVisibility) {
        emailField.visibleProperty().setValue(textfieldVisibility);
        emailTextContainer.visibleProperty().setValue(labelVisibility);
        textFieldContainer.visibleProperty().setValue(textfieldVisibility);
        labelContainer.visibleProperty().setValue(labelVisibility);
    }
}
