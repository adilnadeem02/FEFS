module com.example.fefs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.fefs to javafx.fxml;
    exports com.example.fefs;
}