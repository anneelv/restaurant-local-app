package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.anneelv.burritokingv2.Database.CustomerDb;
import org.anneelv.burritokingv2.Models.Customer;
import org.anneelv.burritokingv2.Models.UserSession;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Controller class responsible for handling upgrade VIP scene
 * and functionality.
 */
public class UpgradeVIPController extends ControllerInit {
    private UserSession session;
    private Customer customer;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();
    private final Validator validator = new Validator();

    @FXML
    private Label errorLabel;
    @FXML
    private TextField emailTF;

    /*
     * Initializes the user session and UI.
     */
    @Override
    public void initUserSession(UserSession userSession) {
        this.session = userSession;

        // Initialize the UI with the user information
        initializeUI();
    }

    /*
     * Initializes the UI with user information.
     */
    @Override
    protected void initializeUI() {
        if (session != null) {
            // Get the authenticated user from the UserSession
            customer = session.getCustomer();
        }
    }

    /*
     * Action handler for 'No' button.
     */
    public void noBtnOnAction(ActionEvent event) throws IOException{
        switchSceneController.dashboard(event, session);
    }

    /*
     * Action handler for 'Yes' button.
     */
    public void yesBtnOnAction(ActionEvent event) {
        if(!emailTF.getText().isBlank()){
            if (validator.isValidEmail(emailTF.getText())){
                updateUserStatus();
                showSuccessPopup(event);
            }
            else{
                errorLabel.setText("Please input a valid email address");
            }
        } else {
            errorLabel.setText("Please input your email address");
        }
    }

    /*
     * Shows a success popup and redirects to the login page.
     */
    private void showSuccessPopup(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upgrade to VIP");
        alert.setContentText("Upgrade to VIP successful. You will be redirected to the login page.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    switchSceneController.login(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /*
     * Updates the user status to VIP.
     */
    private void updateUserStatus(){
        CustomerDb customerDB = new CustomerDb();
        customerDB.updateStatus(customer.getUsername());
    }
}
