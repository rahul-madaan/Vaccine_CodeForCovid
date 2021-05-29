package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class mainPageController implements Initializable {
    private DbHandler dbHandler;
    @FXML
    private RadioButton loginRadioButton;
    @FXML
    private RadioButton signupRadioButton;
    @FXML
    private Label loginSignupLabel;
    private ToggleGroup loginSignupToggleGroup;
    @FXML
    private Button loginSignupButton;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label passwordLabel;
    private Connection conn;
    @FXML
    private Label errorLabel;

    public static String activeUserPhoneNumber = "00";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbHandler = new DbHandler();
        loginSignupToggleGroup = new ToggleGroup();
        loginRadioButton.setToggleGroup(loginSignupToggleGroup);
        signupRadioButton.setToggleGroup(loginSignupToggleGroup);
        signupRadioButton.setSelected(true);
    }

    public void loginSignupToggle() {
        if (signupRadioButton.isSelected()) {
            //what changes when signup radio button clicked
            //passwordLabel.setText("Set new password");
            //loginSignupLabel.setText("USER SIGN-UP");
            loginSignupButton.setText("REGISTER");
            passwordTextField.setPromptText("Enter new password");
        } else if (loginRadioButton.isSelected()) {
            //what changes when login radio button is clicked
            //passwordLabel.setText("Password");
            //passwordTextField.setPrefHeight(40);
            //passwordTextField.setPrefWidth(200);
            //loginSignupLabel.setText("USER LOGIN");
            passwordTextField.setPromptText("Enter password");
            loginSignupButton.setText("LOGIN");

        }
    }

    public void faqButtonClick(ActionEvent event) throws IOException {
        Parent scene2Parent = FXMLLoader.load(getClass().getResource("CFC_FAQ.fxml"));
        Scene faqScene = new Scene(scene2Parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(faqScene);
        window.show();
    }
    public void loginSignupButtonClickSuccess(ActionEvent event) throws IOException {
        Parent scene2Parent = FXMLLoader.load(getClass().getResource("allMembersScene.fxml"));
        Scene addMembersScene = new Scene(scene2Parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(addMembersScene);
        window.show();
    }

    @FXML
    public void loginSignupButtonClick(ActionEvent event) throws SQLException, IOException {
        String enteredPhoneNumber = phoneNumberTextField.getText();
        String enteredPassword = passwordTextField.getText();
        //if sign up, then check if new password meets the requirements
        //check if number entered is of 10 digit only and do not contain any letters
        if (loginRadioButton.isSelected()) {
            String queryBoth = "SELECT * from login_users where Phonenumber ='" + enteredPhoneNumber + "' and Password = '" + enteredPassword + "' ;";
            conn = dbHandler.getConnection();
            ResultSet bothSet = conn.createStatement().executeQuery(queryBoth);

            String queryPhoneNumber = "SELECT * from login_users where Phonenumber ='" + enteredPhoneNumber + "';";
            ResultSet phoneNumberSet = conn.createStatement().executeQuery(queryPhoneNumber);
            if (phoneNumberSet.next() == false) {
                errorLabel.setVisible(true);
                errorLabel.setText("You are not registered!");
                errorLabel.setStyle(" -fx-background-color: #ff5c5c; -fx-text-fill: black");
            } else if (bothSet.next() == false) {
                errorLabel.setVisible(true);
                errorLabel.setText("enter correct password!");
                errorLabel.setStyle(" -fx-background-color: #ff5c5c; -fx-text-fill: black");
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Login Successful!");
                errorLabel.setStyle(" -fx-background-color: green; -fx-text-fill: white");
                mainPageController.activeUserPhoneNumber = enteredPhoneNumber;
                loginSignupButtonClickSuccess(event);
            }
        } else if (signupRadioButton.isSelected()) {
            conn = dbHandler.getConnection();
            String queryPhoneNumber = "SELECT * from login_users where Phonenumber ='" + enteredPhoneNumber + "';";
            ResultSet phoneNumberSet = conn.createStatement().executeQuery(queryPhoneNumber);
            if (phoneNumberSet.next() == true) {
                errorLabel.setVisible(true);
                errorLabel.setText("You are already registered!");
                errorLabel.setStyle(" -fx-background-color: #ff5c5c; -fx-text-fill: black");
            } else {
                String queryInsert = "INSERT INTO login_users (PhoneNumber, Password) VALUES ('" + enteredPhoneNumber + "','" + enteredPassword + "');";
                conn = dbHandler.getConnection();
                conn.createStatement().executeUpdate(queryInsert);
                errorLabel.setVisible(true);
                errorLabel.setStyle(" -fx-background-color: green; -fx-text-fill: white");
                errorLabel.setText("Registered Successfully!");
                mainPageController.activeUserPhoneNumber = enteredPhoneNumber;
                loginSignupButtonClickSuccess(event);
            }
        }
    }
}
