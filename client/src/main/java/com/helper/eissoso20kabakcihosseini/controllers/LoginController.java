package com.helper.eissoso20kabakcihosseini.controllers;

import java.io.IOException;

import com.helper.eissoso20kabakcihosseini.App;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
