package com.example.project_student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/student";
    String user = "postgres";
    String pass = "000000@Ss";

    public static final String TABLE_STUDENT = "std";
    public static final String COLUMN_STUDENT_ID = "std_id";
    public static final String COLUMN_STUDENT_FIRST_NAME = "first_name";
    public static final String COLUMN_STUDENT_LAST_NAME = "last_name";
    public static final String COLUMN_STUDENT_PASSWORD = "password";
    public static final String COLUMN_STUDENT_EMAIL = "email";
    public static final String COLUMN_STUDENT_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_STUDENT_SPECIALIZE = "specialize";


    public static final String INSERT_STUDENT = "INSERT INTO " + TABLE_STUDENT +
            '(' + COLUMN_STUDENT_FIRST_NAME + ", " + COLUMN_STUDENT_LAST_NAME +
            ", " + COLUMN_STUDENT_PASSWORD + "," + COLUMN_STUDENT_EMAIL + ", " +
            COLUMN_STUDENT_PHONE_NUMBER + ", " + COLUMN_STUDENT_SPECIALIZE + ") VALUES(? , ? , ? , ? , ?, ?)";


    public static final String LOGIN_QUERY = "SELECT * FROM " + TABLE_STUDENT + " WHERE " +
            COLUMN_STUDENT_EMAIL + "=? and " + COLUMN_STUDENT_PASSWORD + " =? ";


    private static final String SELECT_BY_EMAIL_QUERY = "SELECT * FROM " + TABLE_STUDENT +
            " WHERE " + COLUMN_STUDENT_EMAIL + " =?";

    private static final String SELECT_ALL_QUERY = "SELECT * FROM " + TABLE_STUDENT;

    private static final String UPDATE_EMAIL_STUDENT_QUERY = "UPDATE " + TABLE_STUDENT + " SET " +
            COLUMN_STUDENT_EMAIL + " = ?" + " WHERE " + COLUMN_STUDENT_ID + "= ?";


//    private static final String UPDATE_FIRSTNAME_STUDENT_QUERY = "UPDATE " + TABLE_STUDENT + " SET " +
//            COLUMN_STUDENT_FIRST_NAME + " = ?" + " WHERE " + COLUMN_STUDENT_ID + "= ?";
//    private static final String UPDATE_LASTNAME_STUDENT_QUERY = "UPDATE " + TABLE_STUDENT + " SET " +
//            COLUMN_STUDENT_LAST_NAME + " = ?" + " WHERE " + COLUMN_STUDENT_ID + "= ?";
//    private static final String UPDATE_PHONE_NUMBER_STUDENT_QUERY = "UPDATE " + TABLE_STUDENT + " SET " +
//            COLUMN_STUDENT_PHONE_NUMBER + " = ?" + " WHERE " + COLUMN_STUDENT_ID + "= ?";
//


    private static final String DELETE_STUDENT_QUERY = "DELETE FROM " + TABLE_STUDENT + " WHERE " + COLUMN_STUDENT_ID + " =?";


    private Connection conn;

    private PreparedStatement insertIntoStudent;
    private PreparedStatement loginQuery;
    private PreparedStatement getInfo;
    private PreparedStatement getAllStudent;
    private PreparedStatement updateEmail;
    private PreparedStatement deleteQuery;

    private static Datasource instance = new Datasource();


    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
    }


    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, user, pass);
            insertIntoStudent = conn.prepareStatement(INSERT_STUDENT);
            loginQuery = conn.prepareStatement(LOGIN_QUERY);
            getInfo = conn.prepareStatement(SELECT_BY_EMAIL_QUERY);
            getAllStudent = conn.prepareStatement(SELECT_ALL_QUERY);
            updateEmail = conn.prepareStatement(UPDATE_EMAIL_STUDENT_QUERY);
            deleteQuery = conn.prepareStatement(DELETE_STUDENT_QUERY);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }


    public void close() {
        try {
            if (insertIntoStudent != null) {
                insertIntoStudent.close();
            }
            if (loginQuery != null) {
                loginQuery.close();
            }
            if (getInfo != null) {
                getInfo.close();
            }
            if (getAllStudent != null) {
                getAllStudent.close();
            }
            if (updateEmail != null) {
                updateEmail.close();
            }
            if (deleteQuery != null) {
                deleteQuery.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public String login(String email, String password) {
        try {
            loginQuery.setString(1, email);
            loginQuery.setString(2, password);
            ResultSet results = loginQuery.executeQuery();
            if (results.next()) {
                if (results.getString("email").equals(email) && results.getString("password").equals(password)) {

                    return "success";
                }
            } else {
                return "fail";
            }

        } catch (SQLException ex) {
            System.out.println("Query failed: " + ex.getMessage());
        }
        return null;
    }

    public List<Student> getAll() {

        try (ResultSet results = getAllStudent.executeQuery()) {

            List<Student> students = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                Student student = new Student();
                student.setId(results.getInt(COLUMN_STUDENT_ID));
                student.setFirstName(results.getString(COLUMN_STUDENT_FIRST_NAME));
                student.setLastName(results.getString(COLUMN_STUDENT_LAST_NAME));
                student.setEmail(results.getString(COLUMN_STUDENT_EMAIL));
                student.setPhoneNumber(results.getString(COLUMN_STUDENT_PHONE_NUMBER));
                student.setPassword(results.getString(COLUMN_STUDENT_PASSWORD));
                student.setSpecialize(results.getString(COLUMN_STUDENT_SPECIALIZE));
                students.add(student);
            }

            return students;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    public Student getInfo(String email) {
        try {
            loginQuery.setString(1, email);
            ResultSet results = loginQuery.executeQuery();
            Student student = new Student();
            if (results.next()) {
                if (results.getString(COLUMN_STUDENT_EMAIL).equals(email)) {
                    student.setEmail(results.getString(COLUMN_STUDENT_EMAIL));
                    student.setFirstName(results.getString(COLUMN_STUDENT_FIRST_NAME));
                    student.setLastName(COLUMN_STUDENT_LAST_NAME);
                    student.setPassword(COLUMN_STUDENT_PASSWORD);
                    student.setSpecialize(COLUMN_STUDENT_SPECIALIZE);
                    student.setPhoneNumber(COLUMN_STUDENT_PHONE_NUMBER);
                }
                return student;
            } else {
                System.out.println(results);
                System.out.println("fail");
                return null;
            }

        } catch (SQLException ex) {
            System.out.println("Query failed: " + ex.getMessage());
            return null;
        }

    }

    /*   try{
            queryUpdate.setInt(1, Integer.parseInt(age));
            queryUpdate.setString(2,fname);
            queryUpdate.setString(3,lname);
            queryUpdate.setString(4,email);
            int res=queryUpdate.executeUpdate();
            if(res==1){
                System.out.println("success");
                return true;
            }else return false;



        } catch (SQLException e) {
            System.out.println("failed "+e.getMessage());
            return false;
        }*/

    public boolean updateStudentEmail(int id, String newEmail) {


        try {
            updateEmail.setString(1, newEmail);
            updateEmail.setInt(2, id);
            int affectedRecords = updateEmail.executeUpdate();

            return affectedRecords == 1;

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int id) {
        try {
            deleteQuery.setInt(1, id);
            int resultSet = deleteQuery.executeUpdate();
            if (resultSet == 1) {
                System.out.println("success");
                return true;
            } else {
                System.out.println("failed");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }


    public String insertStudent(String first_name, String last_name, String password,
                                String email, String phone_number, String specialize) {

        try {
            insertIntoStudent.setString(1, first_name);
            insertIntoStudent.setString(2, last_name);
            insertIntoStudent.setString(3, password);
            insertIntoStudent.setString(4, email);
            insertIntoStudent.setString(5, phone_number);
            insertIntoStudent.setString(6, specialize);
            insertIntoStudent.executeUpdate();
            return "successfully added";
        } catch (SQLException e) {
            return "failed" + e.getMessage();
        }
    }

}
