package com.helper.eissoso20kabakcihosseini.controllers;

import com.helper.eissoso20kabakcihosseini.App;
import com.helper.eissoso20kabakcihosseini.utils.APICaller;
import com.helper.eissoso20kabakcihosseini.utils.AlertHelper;
import com.helper.eissoso20kabakcihosseini.utils.Validator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        Task<Void> task = APICaller.login(email, password);
        task.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldMessage, String newMessage) {
                String statusCode = newMessage.split(",")[0];
                String message = newMessage.split(",")[1];
                if (statusCode.equals("200")) {
                    try {
                        System.out.println(message);
                        App.setRoot("dashboard");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (statusCode.equals("401")) {
                    System.out.println(message);
                    AlertHelper.showLoadingDialog();
                }
            }
        });
        new Thread(task).start();

    }

    @FXML
    private void handleUserRegistration() throws IOException {
        App.setRoot("register");
    }


    // Hilfsfunktion, um zu überprüfen ob das emailField eine nicht richtige email beinhaltet, wenn ja zeige fehler an
    private void showEmailError() {
        emailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!Validator.isValidEmail(newValue) || newValue.isEmpty()) {
                    errorLabelEmail.setVisible(true);
                    emailField.setFocusColor(Color.RED);
                } else {
                    emailField.setFocusColor(Color.rgb(0, 147, 217));
                    errorLabelEmail.setVisible(false);
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
                    errorLabelPassword.setVisible(true);
                    passwordField.setFocusColor(Color.RED);
                } else {
                    passwordField.setFocusColor(Color.rgb(0, 147, 217));
                    errorLabelPassword.setVisible(false);
                }
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showEmailError();
        showPasswordError();
        loginButton.disableProperty().bind(emailField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
    }
}
