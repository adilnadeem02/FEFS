package com.example.fefs;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainPageController {
    @FXML
    public TextField email;
    public PasswordField pass;
    public ComboBox<String> role;
    public Button login;

    public void initialize() {
        role.getItems().addAll("Student", "Faculty", "Administrator");
        role.setValue("Student");
    }

    @FXML void login()
    {
        FEFS fefs = FEFS.getInstance();
        if(fefs.login(email.getText(), pass.getText(), role.getButtonCell().getText()))
        {
            System.out.println("SIGNED IN");
        }

        else
        {
            System.out.println("Failed");
        }
    }
}