package org.anneelv.burritokingv2.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.anneelv.burritokingv2.Models.Customer;
import org.anneelv.burritokingv2.Models.UserSession;

import java.io.IOException;


/*
 * Controller class responsible for managing Profile scene and functionalities.
 */
public class ProfileController extends ControllerInit{
    private UserSession session;
    private Customer customer;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();

    @FXML
    private Label profileUpdateMessage;
    @FXML
    private Label VIPLabel;
    @FXML
    private TextField usernameTF;
    @FXML
    private TextField firstnameTF;
    @FXML
    private TextField lastnameTF;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    @Override
    public void initUserSession(UserSession userSession) {
        this.session = userSession;

        // Initialize the UI with the user information
        initializeUI();
    }

    @Override
    protected void initializeUI() {
        if (session != null) {
            // Get the authenticated user from the UserSession
            customer = session.getCustomer();
            usernameTF.setText(customer.getUsername());
            firstnameTF.setPromptText(customer.getFirstname());
            lastnameTF.setPromptText(customer.getLastname());
        }
        if (customer.getStatus().equals("VIP")){
            VIPLabel.setVisible(false);
        }
    }

    /*
    * Update user profile and send the trigger for UserSessionListener
    * The update is also reflected in database
    * */
    public void updateProfile(){
        if(!firstnameTF.getText().isBlank() || !lastnameTF.getText().isBlank() || !newPasswordField.getText().isBlank() || !confirmPasswordField.getText().isBlank()){
            if(!firstnameTF.getText().isBlank()){
                updateFirstName(firstnameTF.getText());
                profileUpdateMessage.setTextFill(Color.GREEN);
                profileUpdateMessage.setText("First name updated!");
                firstnameTF.setPromptText(customer.getFirstname());
            }
            if(!lastnameTF.getText().isBlank()){
                updateLastName(lastnameTF.getText());
                profileUpdateMessage.setTextFill(Color.GREEN);
                profileUpdateMessage.setText("Last name updated!");
                lastnameTF.setPromptText(customer.getLastname());
            }
            if(!newPasswordField.getText().isBlank()){
                if(!confirmPasswordField.getText().isBlank()){
                    updatePassword(newPasswordField.getText(), confirmPasswordField.getText());
                } else{
                    profileUpdateMessage.setTextFill(Color.RED);
                    profileUpdateMessage.setText("Please confirm your password");
                }
            }
            session.updateCustomer(customer);
            session.emitUserSessionEvent();
        } else{
            profileUpdateMessage.setTextFill(Color.RED);
            profileUpdateMessage.setText("Nothing to be updated.");
        }
    }

    public void updateFirstName(String firstname) {
        customer.setFirstname(firstname);
    }

    public void updateLastName(String lastname) {
        customer.setLastname(lastname);
    }

    /*
    * Ensure that the new password is properly confirmed and update the password
    * */
    public void updatePassword(String newPassword, String confirmPassword) {
        if (newPassword.equals(confirmPassword)){
            customer.setPassword(newPassword);
            profileUpdateMessage.setTextFill(Color.GREEN);
            profileUpdateMessage.setText("Password updated!");
        } else {
            profileUpdateMessage.setTextFill(Color.RED);
            profileUpdateMessage.setText("Your password is not matching");
        }
    }

    /*
    * Navigate the user to upgrade their account to "VIP"
    * */
    public void upgradeVIP(MouseEvent event) throws IOException {
        switchSceneController.upgradeVIP(event, session);
    }
}
