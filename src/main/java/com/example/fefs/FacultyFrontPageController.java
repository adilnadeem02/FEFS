package com.example.fefs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FacultyFrontPageController
{
    @FXML Button signOutButton;

    @FXML
    private TableView<FeedbackModel> table;
    static int faculty_id;

    public void setFacultyID(int faculty_id) {
        this.faculty_id = faculty_id;
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

    public void loadData() throws SQLException
    {
        List<String[]> feedback = FEFS.getInstance().getFeedback(faculty_id);
        if(table == null)
            table = new TableView<FeedbackModel>();

        if (table.getColumns().isEmpty()) {
            TableColumn<FeedbackModel, Integer> feedbackIdCol = new TableColumn<>("Feedback ID");
            feedbackIdCol.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));

            TableColumn<FeedbackModel, Integer> courseIdCol = new TableColumn<>("Course ID");
            courseIdCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));

            TableColumn<FeedbackModel, Integer> ratingCol = new TableColumn<>("Rating");
            ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

            TableColumn<FeedbackModel, String> commentsCol = new TableColumn<>("Comments");
            commentsCol.setCellValueFactory(new PropertyValueFactory<>("comments"));

            TableColumn<FeedbackModel, String> createdAtCol = new TableColumn<>("Created At");
            createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

            table.getColumns().addAll(feedbackIdCol, courseIdCol, ratingCol, commentsCol, createdAtCol);
        }

        ObservableList<FeedbackModel> feedbackData = FXCollections.observableArrayList();
        for (String[] feedbacks : feedback) {
            feedbackData.add(new FeedbackModel(
                    Integer.parseInt(feedbacks[0]),
                    Integer.parseInt(feedbacks[1]),
                    Integer.parseInt(feedbacks[2]),
                    feedbacks[3],
                    feedbacks[4]
            ));
        }
        table.setItems(feedbackData);
    }
}
