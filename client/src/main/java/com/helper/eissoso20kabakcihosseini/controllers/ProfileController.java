package com.helper.eissoso20kabakcihosseini.controllers;

import com.helper.eissoso20kabakcihosseini.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private JFXTextField emailField, cityField, streetField, streetNumberField, zipcodeField;

    @FXML
    private JFXButton updateButton, passwordResetButton;

    @FXML
    private JFXToggleButton editButton;

    @FXML
    private Text usernameText;

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
    }
}
