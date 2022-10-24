package com.example.project_student;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private TextField fName, lName, email, phone, specialize, email2;
    @FXML
    private PasswordField passwordField , passwordField2;
    @FXML
    private Button register;
    @FXML
    private Label hal;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TableView studentTable;

    @FXML
    private TextField updateEmailField, updateFNameField , updatePhoneField;
    Controller controller ;


    @FXML
    private void addStudent() throws IOException{
        String result = Datasource.getInstance().insertStudent(
                fName.getText(),
                lName.getText(),
                passwordField2.getText(),
                email2.getText(),
                phone.getText(),
                specialize.getText()
        );
        System.out.println(result);
        if (result.equals("successfully added")) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("info-page.fxml"));
            Parent root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.listStudents();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Students page");
            stage.setScene(scene);
            stage.show();
        }else
            System.out.println("retry");


    }

    @FXML
    private void login() throws IOException {
       String result = Datasource.getInstance().login(
                email.getText(),
               passwordField.getText()
        );

        if (result.equals("success")){
//            Student student = Datasource.getInstance().getInfo(email.getText());
//            System.out.println(student.getEmail());

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("info-page.fxml"));
            Parent root = fxmlLoader.load();
            controller= fxmlLoader.getController();
            controller.listStudents();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Students page");
            stage.setScene(scene);
            stage.show();
            //infoPage(student);

        }
        else
            System.out.println("failed to login please try again");

    }

    @FXML
    private void infoPage(Student student){
        //hal.setText(student.getEmail());
    }

    @FXML
    public void listStudents() {
        Task<ObservableList<Student>> task = new GetAllStudentsTask();
        studentTable.itemsProperty().bind(task.valueProperty());
        progressBar.progressProperty().bind(task.progressProperty());

        progressBar.setVisible(true);

        task.setOnSucceeded(e -> progressBar.setVisible(false));
        task.setOnFailed(e -> progressBar.setVisible(false));

        new Thread(task).start();
    }

    @FXML
    public void updateStd() {

        final Student student= (Student) studentTable.getSelectionModel().getSelectedItem();


        if(student == null) {
            System.out.println("NO Student SELECTED");
            return;
        }
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().updateStudentEmail(student.getId(), updateEmailField.getText());
            }
        };

        task.setOnSucceeded(e -> {
            if(task.valueProperty().get()) {
                student.setEmail(updateEmailField.getText());
                studentTable.refresh();
            }
        });

        new Thread(task).start();
    }

    @FXML
    public void deleteStd() {

        final Student student= (Student) studentTable.getSelectionModel().getSelectedItem();
        
        if(student == null) {
            System.out.println("NO Student SELECTED");
            return;
        }
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().deleteStudent(student.getId());
            }
        };
        task.setOnSucceeded(e -> {
            if(task.valueProperty().get()) {
                studentTable.getItems().remove(student);
                studentTable.refresh();
            }
        });
        new Thread(task).start();


    }
}

    class GetAllStudentsTask extends Task {

        @Override
        public ObservableList<Student> call() {
            return FXCollections.observableArrayList
                    (Datasource.getInstance().getAll());
        }
    }


