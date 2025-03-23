package com.example.fefs;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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
        if(fefs.login(email.getText(), pass.getText(), role.getValue()))
        {
            if(role.getValue().equals("Student"))
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("student-front-page.fxml"));
                    Scene scene = new Scene(loader.load());

                    Stage primaryStage = (Stage) login.getScene().getWindow();

                    primaryStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if(role.getValue().equals("Faculty")) {
                int id = FEFS.getInstance().getInstructorID(email.getText(), pass.getText(), role.getValue());

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("faculty-front-page.fxml"));
                    Scene scene = new Scene(loader.load());

                    FacultyFrontPageController controller = loader.getController();
                    controller.setFacultyID(id);

                    Stage primaryStage = (Stage) login.getScene().getWindow();
                    primaryStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if(role.getValue().equals("Administrator"))
            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-front-page.fxml"));
                    Scene scene = new Scene(loader.load());

                    Stage primaryStage = (Stage) login.getScene().getWindow();

                    primaryStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("INCORRECT CREDENTIALS!");
            alert.showAndWait();
        }
    }
}