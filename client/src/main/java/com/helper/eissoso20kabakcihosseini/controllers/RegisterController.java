package com.helper.eissoso20kabakcihosseini.controllers;

import java.io.IOException;

import com.helper.eissoso20kabakcihosseini.App;
import javafx.fxml.FXML;

public class RegisterController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("login");
    }
}