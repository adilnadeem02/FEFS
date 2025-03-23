module com.example.fefs {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fefs to javafx.fxml;
    exports com.example.fefs;
}