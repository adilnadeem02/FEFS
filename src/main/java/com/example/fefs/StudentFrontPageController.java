package com.example.fefs;

import com.sun.tools.javac.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentFrontPageController
{
    @FXML Button signOutButton;

    @FXML
    ComboBox<String> courseName;
    @FXML
    ComboBox<String> instructor;
    @FXML
    ComboBox<String> rating;
    @FXML
    TextField comments;
    @FXML
    Button submit;

    int instructorID;
    int courseID;

    @FXML
    private TableView<NotifClass> table;

    public void initialize() throws SQLException {
        List<String[]> courses = FEFS.getInstance().returnCourses();

        for (String[] course : courses) {
            courseName.getItems().add(course[0]); // course_name
            courseID = Integer.parseInt(course[1]); // course_id
        }

        courseName.setOnAction(event -> {
            String selectedCourse = courseName.getValue();
            if (selectedCourse != null) {
                try {
                    populateInstructors(selectedCourse);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        rating.getItems().addAll("1", "2", "3", "4", "5");
    }

    private void populateInstructors(String selectedCourse) throws SQLException {

        instructor.getItems().clear();
        List<String[]> instructors = FEFS.getInstance().getInstructors(selectedCourse);

        for (String[] instructorData : instructors) {
            instructor.getItems().add(instructorData[0]); // Instructor Name
            instructorID = Integer.parseInt(instructorData[1]); // Instructor ID
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

    @FXML void submitFeedback()
    {
        FEFS.submitFeedback(instructorID, courseID, Integer.parseInt(rating.getValue()), comments.getText());
        courseName.getItems().clear();
        instructor.getItems().clear();
        comments.setText("");
        rating.getItems().clear();
        rating.getItems().addAll("1", "2", "3", "4", "5");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Feedback Recorded!");
        alert.showAndWait();
    }

    @FXML
    void viewNotifs() {
        List<String[]> feedback = FEFS.getInstance().getNotifs();

        if (table == null)
            table = new TableView<>();

        if (table.getColumns().isEmpty()) {
            TableColumn<NotifClass, Integer> notifIdCol = new TableColumn<>("Notification ID");
            notifIdCol.setCellValueFactory(new PropertyValueFactory<>("notificationId"));

            TableColumn<NotifClass, String> messageCol = new TableColumn<>("Message");
            messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

            TableColumn<NotifClass, String> createdAtCol = new TableColumn<>("Created At");
            createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

            table.getColumns().addAll(notifIdCol, messageCol, createdAtCol);
        }

        ObservableList<NotifClass> notifData = FXCollections.observableArrayList();
        for (String[] feedbacks : feedback) {
            notifData.add(new NotifClass(
                    Integer.parseInt(feedbacks[0]),
                    feedbacks[1],
                    feedbacks[2]
            ));
        }

        table.setItems(notifData);
    }

}
