package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.anneelv.burritokingv2.Database.CustomerDb;

import java.io.IOException;

/*
 * Controller class responsible for handling Signup scene and functions.
 */
public class SignupController {
    private CustomerDb customerDB;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();

    @FXML
    private Label signupMessage;
    @FXML
    private TextField usernameTF;
    @FXML
    private TextField firstnameTF;
    @FXML
    private TextField lastnameTF;
    @FXML
    private PasswordField passwordField;

    /*
     * Handles the action when the register button is clicked.
     */
    public void registerBtnOnAction(){
        if(firstnameTF.getText().isBlank()){
            signupMessage.setText("Please input your first name");
        } else if(lastnameTF.getText().isBlank()){
            signupMessage.setText("Please input your last name");
        } else if(usernameTF.getText().isBlank()){
            signupMessage.setText("Please input your username");
        } else if(passwordField.getText().isBlank()){
            signupMessage.setText("Please input your password");
        } else if (!firstnameTF.getText().isBlank() && !lastnameTF.getText().isBlank() && !usernameTF.getText().isBlank() && !passwordField.getText().isBlank()){
            validateNewUser();
        }
    }

    /*
     * Switches to the login scene.
     */
    public void switchToLogin(ActionEvent event) throws IOException {
        switchSceneController.login(event);
    }

    /*
     * Validates if the new username is unique.
     */
    public void validateNewUser() {
        customerDB = new CustomerDb();
        if (customerDB.isUsernameUnique(usernameTF.getText().toLowerCase())){
            registerNewUser();
            signupMessage.setTextFill(Color.GREEN);
            signupMessage.setText("Your account has been successfully registered");
        } else {
            signupMessage.setTextFill(Color.RED);
            signupMessage.setText("There's an existing account with this username");
        }
    }


    /*
     * Registers a new user to the database
     */
    public void registerNewUser() {
        customerDB = new CustomerDb();
        customerDB.insertNewUser(usernameTF.getText().toLowerCase(), firstnameTF.getText(), lastnameTF.getText(), passwordField.getText());
    }
}
