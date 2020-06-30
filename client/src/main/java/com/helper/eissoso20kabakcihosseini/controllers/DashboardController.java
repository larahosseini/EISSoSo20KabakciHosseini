package com.helper.eissoso20kabakcihosseini.controllers;

import com.helper.eissoso20kabakcihosseini.App;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private JFXButton profileButton;

    @FXML
    private JFXButton tasksButton;

    @FXML
    private void openProfilePage(){
        try {
            App.setRoot("profile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openTasksPage(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profileButton.setText("");
        tasksButton.setText("");
    }
}
