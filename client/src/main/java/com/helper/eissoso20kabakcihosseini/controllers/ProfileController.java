package com.helper.eissoso20kabakcihosseini.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private JFXTextField emailField, cityField, streetField, streetNumberField, zipcodeField;

    @FXML
    private JFXButton updateButton, createTaskButton;

    @FXML
    private void updateUserData(){

    }

    @FXML
    private void createTask(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
