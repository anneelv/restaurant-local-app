package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.anneelv.burritokingv2.Models.Customer;
import org.anneelv.burritokingv2.Models.UserSession;

import java.io.IOException;

/*
 * Controller class for managing Dashboard scene and functionalities.
 */
public class DashboardController extends ControllerInit implements UserSessionListener{
    private UserSession session;
    private Customer customer;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();

    @FXML
    private StackPane contentArea;
    @FXML
    private Label fullnameLabel;
    @FXML
    private Button pointHistoryButton;

    @Override
    public void initUserSession(UserSession userSession) throws IOException {
        this.session = userSession;
        session.addListener(this);

        // Initialize the UI with the user information
        initializeUI();
        goToOrder();
    }

    @Override
    protected void initializeUI() {
        if (session != null) {
            // Get the authenticated user from the UserSession
            customer = session.getCustomer();
            updateFullNameLabel(customer);
            session.setupOrderHistory(customer.getUsername());
            if (!customer.getStatus().equals("VIP")){
                pointHistoryButton.setVisible(false);
            }
        }
    }

    public void goToProfile() throws IOException {
        switchSceneController.profile(contentArea, session);
    }

    public void goToFoodMenu() throws IOException {
        switchSceneController.foodMenu(contentArea, session);
    }

    public void goToOrder() throws IOException {
        switchSceneController.order(contentArea, session);
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        switchSceneController.login(actionEvent);
    }

    public void goToPointHistory() throws IOException {
        switchSceneController.pointHistory(contentArea, session);
    }

    /*
     * Callback method called when the user session is updated.
     */
    @Override
    public void onUserSessionUpdated(Customer updatedCustomer) {
        updateFullNameLabel(updatedCustomer);
    }

    /*
     * Updates the full name label with the customer's information.
     */
    private void updateFullNameLabel(Customer customer){
        fullnameLabel.setText(customer.getFirstname() + " " + customer.getLastname());
    }
}
