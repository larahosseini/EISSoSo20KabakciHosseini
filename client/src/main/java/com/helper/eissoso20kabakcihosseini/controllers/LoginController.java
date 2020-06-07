package com.helper.eissoso20kabakcihosseini.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.helper.eissoso20kabakcihosseini.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.commons.validator.routines.EmailValidator;

public class LoginController implements Initializable {

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private VBox emailErrorContainer, passwordErrorContainer;

    @FXML
    private Label errorLabelEmail;

    @FXML
    private Label errorLabelPassword;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXPasswordField passwordField;


    @FXML
    private void handleLogin() throws IOException {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        System.out.println("Email: " + email + " Password: " + password);
    }

    // Hilfsfunktion, um zu überprüfen ob das emailField eine nicht richtige email beinhaltet, wenn ja zeige fehler an
    private void showEmailError() {
        emailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!isValidEmail(newValue) || newValue.isEmpty()) {
                    System.out.println("Email Error");
                    emailErrorContainer.setVisible(true);
                    emailField.setFocusColor(Color.RED);
                } else {
                    System.out.println("Email valid");
                    emailField.setFocusColor(Color.rgb(0, 147, 217));
                    emailErrorContainer.setVisible(false);
                }
            }
        });
    }

    // Hilfsfunktion, um zu überprüfen ob das passwordfield leer ist, wenn ja zeige fehler an
    private void showPasswordError() {
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    passwordErrorContainer.setVisible(true);
                    passwordField.setFocusColor(Color.RED);
                } else {
                    passwordField.setFocusColor(Color.rgb(0, 147, 217));
                    passwordErrorContainer.setVisible(false);
                }
            }
        });
    }

    // Hilfsfunktion, um zu überprüfen ob das eine richtige email ist
    private boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showEmailError();
        showPasswordError();
    }
}
