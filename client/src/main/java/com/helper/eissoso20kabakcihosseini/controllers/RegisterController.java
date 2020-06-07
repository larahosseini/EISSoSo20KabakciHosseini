package com.helper.eissoso20kabakcihosseini.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.helper.eissoso20kabakcihosseini.App;
import com.helper.eissoso20kabakcihosseini.utils.APICaller;
import com.helper.eissoso20kabakcihosseini.utils.Validator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class RegisterController implements Initializable {

    @FXML
    private JFXButton createUserButton;

    @FXML
    JFXPasswordField passwordField, confirmPasswordField;

    @FXML
    JFXTextField emailField, streetField, streetNumberField, cityField, zipcodeField;

    @FXML
    private Label errorLabelEmail, errorLabelPassword, errorLabelConfirmPassword,
            errorLabelStreet, errorLabelCity, errorLabelZipcode;

    private String password;
    private String email;
    private String city;
    private String zipcode;
    private String street, streetNumber;

    @FXML
    private void createUser() throws IOException {

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
                    email = newValue.trim();
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
                    password = newValue.trim();
                    passwordField.setFocusColor(Color.rgb(0, 147, 217));
                    errorLabelPassword.setVisible(false);
                }
            }
        });
        showConfirmPasswordError();
    }

    // Hilfsfunktion, um zu überorüfen ob das Passwort zweimal gleich eingegeben wurde
    private void showConfirmPasswordError() {
        confirmPasswordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.equals(password)) {
                    confirmPasswordField.setFocusColor(Color.RED);
                    errorLabelConfirmPassword.setVisible(true);
                } else {
                    confirmPasswordField.setFocusColor(Color.rgb(0, 147, 217));
                    errorLabelConfirmPassword.setVisible(false);
                }
            }
        });
    }

    // Hilfsfunktion, um zu überprüfen ob ein Addresse eingegeben wurde
    private void showAddressError() {
        streetField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    errorLabelStreet.setVisible(true);
                    streetField.setFocusColor(Color.RED);
                    streetNumberField.setFocusColor(Color.RED);
                } else {
                    street = newValue.trim();
                    streetField.setFocusColor(Color.rgb(0, 147, 217));
                    streetNumberField.setFocusColor(Color.rgb(0, 147, 217));
                    errorLabelStreet.setVisible(false);
                }
            }
        });
        streetNumberField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    streetNumber = newValue.trim();
                }
            }
        });
        cityField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                if (newValue.isEmpty()) {
                    errorLabelCity.setVisible(true);
                    cityField.setFocusColor(Color.RED);
                } else {
                    city = newValue.trim();
                    cityField.setFocusColor(Color.rgb(0, 147, 217));
                    errorLabelCity.setVisible(false);
                }
            }
        });
        zipcodeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    errorLabelZipcode.setVisible(true);
                    zipcodeField.setFocusColor(Color.RED);
                } else {
                    zipcode = newValue.trim();
                    zipcodeField.setFocusColor(Color.rgb(0, 147, 217));
                    errorLabelZipcode.setVisible(false);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showEmailError();
        showPasswordError();
        showAddressError();
    }
}