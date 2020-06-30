package com.helper.eissoso20kabakcihosseini.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private JFXButton profileButton;

    @FXML
    private JFXButton tasksButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileButton.setText("");
        tasksButton.setText("");
    }
}