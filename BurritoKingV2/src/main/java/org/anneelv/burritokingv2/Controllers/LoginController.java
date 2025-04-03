package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.anneelv.burritokingv2.Database.CustomerDb;
import org.anneelv.burritokingv2.Models.UserSession;

import java.io.IOException;

/*
 * Controller class for managing Login scene and functionalities.
 */
public class LoginController  {
    private final UserSession model = new UserSession();
    private final SwitchSceneController switchSceneController = new SwitchSceneController();

    @FXML
    private Label loginMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameTF;

    /*
    * Handles action for login button
    * */
    public void loginBtnOnAction(ActionEvent event) throws IOException {
        // Check if username is blank
        if(usernameTF.getText().isBlank()){
            loginMessage.setText("Please input your username");
        }
        // Check if password is blank
        else if(passwordField.getText().isBlank()){
            loginMessage.setText("Please input your password");
        }
        // If both username and password are provided
        else if (!usernameTF.getText().isBlank() && !passwordField.getText().isBlank()) {
            // Validate login credentials
            if (validateLogin()){
                // Activate current user session and switch to dashboard view
                activateCurrentUser(usernameTF.getText());
                switchSceneController.dashboard(event, model);
            }
        }
    }

    /*
    * Handling actions when signup button is clicked
    * */
    public void switchToSignup(ActionEvent event) throws IOException {
        switchSceneController.signup(event);
    }

    /*
    * Validating login credentials by cross-checking with the database
    * */
    public boolean validateLogin(){
        CustomerDb customerDB = new CustomerDb();
        if (customerDB.isValidUser(usernameTF.getText().toLowerCase(), passwordField.getText())){
            loginMessage.setText("Your account is valid");
            return true;
        } else {
            loginMessage.setText("Your account is invalid");
            return false;
        }
    }

    /*
     * Activates the current user session for validated user.
     */
    private void activateCurrentUser(String username){
        model.setupCustomer(username);
    }

}
