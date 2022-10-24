module com.example.project_student {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.project_student to javafx.fxml;
    exports com.example.project_student;
}