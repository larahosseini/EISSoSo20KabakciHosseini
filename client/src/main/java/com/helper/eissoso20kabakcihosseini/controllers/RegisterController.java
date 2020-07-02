package com.helper.eissoso20kabakcihosseini.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.helper.eissoso20kabakcihosseini.App;
import com.helper.eissoso20kabakcihosseini.models.User;
import com.helper.eissoso20kabakcihosseini.utils.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private JFXButton createUserButton, goToLoginButton;

    @FXML
    JFXPasswordField passwordField, confirmPasswordField;

    @FXML
    JFXTextField emailField, streetField, streetNumberField, cityField, zipcodeField;

    @FXML
    private Label errorLabelEmail, errorLabelPassword, errorLabelConfirmPassword,
            errorLabelStreet, errorLabelCity, errorLabelZipcode;

    @FXML
    private JFXTextField activationLinkField;


    private String password;
    private String email;
    private String city;
    private String zipcode;
    private String street, streetNumber;

    @FXML
    private void createUser() throws IOException {
        Task<Void> task = APICaller.createAccount(email, password, city, street, streetNumber, Integer.parseInt(zipcode));
        task.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String statuscode = t1.split(",")[0];
                String message = t1.split(",")[1];
                if (statuscode.equals("201")) {
                    clearTextfield();
                    AlertHelper.showSuccesAlert("Der Account wurde erstellt, bitte überprüfe dein Postfach", "Kopieren Sie den kompletten Aktivierungslink aus der E-Mail und füge Sie den Link unten ein");
                } else if (statuscode.equals("400") || statuscode.equals("500")) {
                    AlertHelper.showErrorAlert(message);
                }
            }
        });
        new Thread(task).start();
    }

    @FXML
    private void goBackToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void goToLoginAfterAccountActivation() throws InterruptedException {
        String link = activationLinkField.getText().trim();
        String trimmedActivationLink = link.split("http://localhost:3000/")[1];
        String id = trimmedActivationLink.split("/")[2];

        Task<Void> activationTask = APICaller.activateAccount(link);
        Task<Void> profileTask = APICaller.getProfile(id);
        handleTaskForActivation(activationTask);
        handleTaskForGettingProfileData(profileTask);

        Thread activationThread = new Thread(activationTask);
        activationThread.start();
        activationThread.join();
        new Thread(profileTask).start();
    }

    private void handleTaskForActivation(Task<?> task) {
        task.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String message) {
                String statuscode = message.split(",")[0];
                String m = message.split(",")[1];
                if (statuscode.equals("200")) {
                    AlertHelper.showLoadingDialog();
                } else if (statuscode.equals("401")) {
                    AlertHelper.showErrorAlert(message);
                }
            }
        });
    }

    private void handleTaskForGettingProfileData(Task<?> task) {
        task.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String statuscode = t1.split(";")[0];
                String message = t1.split(";")[1];
                System.out.println(message);
                if (statuscode.equals("200")) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        SimpleModule module = new SimpleModule();
                        module.addDeserializer(User.class, new UserDeserializer());
                        mapper.registerModule(module);
                        User user = mapper.readValue(message, User.class);
                        Data.saveUser(user);
                        AlertHelper.stage.close();
                        App.setRoot("login");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    System.out.println("User nicht gefunden");
                }
            }
        });
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

    // löscht die eingabe nachdem der Account erstellt wurde
    private void clearTextfield(){
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        cityField.clear();
        streetField.clear();
        streetNumberField.clear();
        zipcodeField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showEmailError();
        showPasswordError();
        showAddressError();
        // um nicht den create button zu drücken bevor alle felder ausgefüllt sind
        BooleanBinding booleanBinding = emailField.textProperty().isEmpty()
                .or(passwordField.textProperty().isEmpty())
                .or(confirmPasswordField.textProperty().isEmpty())
                .or(cityField.textProperty().isEmpty())
                .or(streetField.textProperty().isEmpty())
                .or(streetNumberField.textProperty().isEmpty())
                .or(zipcodeField.textProperty().isEmpty());
        createUserButton.disableProperty().bind(booleanBinding);
        goToLoginButton.disableProperty().bind(activationLinkField.textProperty().isEmpty());
    }
}
