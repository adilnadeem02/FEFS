package com.example.fefs;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdminFrontPageController
{
    @FXML Button signOutButton;

    @FXML TextField f_name;
    @FXML TextField f_email;
    @FXML TextField f_pass;
    @FXML TextField f_depart;
    @FXML Button f_add;
    @FXML Button f_remove;

    @FXML TextField s_name;
    @FXML TextField s_email;
    @FXML TextField s_pass;
    @FXML TextField s_batch;
    @FXML Button s_add;
    @FXML Button s_remove;

    @FXML TextField notifText;
    @FXML Button SendNotif;

    @FXML void add_faculty()
    {
        if(FEFS.getInstance().addFaculty(f_name.getText(), f_email.getText(), f_pass.getText(),f_depart.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText(null);
            alert.setContentText("FACULTY ADDED!");
            alert.showAndWait();
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("AN ERROR OCCURRED!");
            alert.showAndWait();
        }
    }

    @FXML void rem_faculty()
    {
        if(FEFS.getInstance().remFaculty(f_name.getText(), f_email.getText(), f_pass.getText(),f_depart.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText(null);
            alert.setContentText("FACULTY REMOVED!");
            alert.showAndWait();
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("AN ERROR OCCURRED!");
            alert.showAndWait();
        }
    }

    @FXML void add_student()
    {
        if(FEFS.getInstance().addStudent(s_name.getText(), s_email.getText(), s_pass.getText(),s_batch.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText(null);
            alert.setContentText("STUDENT ADDED!");
            alert.showAndWait();
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("AN ERROR OCCURRED!");
            alert.showAndWait();
        }
    }

    @FXML void rem_student()
    {
        if(FEFS.getInstance().remStudent(s_name.getText(), s_email.getText(), s_pass.getText(),s_batch.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText(null);
            alert.setContentText("STUDENT REMOVED!");
            alert.showAndWait();
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("AN ERROR OCCURRED!");
            alert.showAndWait();
        }
    }

    @FXML void signOut()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
            Scene scene = new Scene(loader.load());

            Stage primaryStage = (Stage) signOutButton.getScene().getWindow();

            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void sendNotif()
    {
        if(FEFS.getInstance().sendNotif(notifText.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SUCCESS");
            alert.setHeaderText(null);
            alert.setContentText("Notifcations Sent!");
            alert.showAndWait();
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("AN ERROR OCCURRED!");
            alert.showAndWait();
        }
    }

}
