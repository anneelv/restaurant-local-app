package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.anneelv.burritokingv2.Database.OrdersDb;
import org.anneelv.burritokingv2.Models.Customer;
import org.anneelv.burritokingv2.Models.Order;
import org.anneelv.burritokingv2.Models.UserSession;

import java.io.IOException;

/*
* Controller class responsible for managing PointRedemption scene and functionalities.
* */
public class PointRedemptionController extends ControllerInit {
    private UserSession session;
    private Customer customer;
    private Order order;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();
    private final Validator validator = new Validator();
    private int pointsToRedeem;

    @FXML
    private Label availPointsLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField redeemPointTF;

    @Override
    public void initUserSession(UserSession userSession) {
        this.session = userSession;

        // Initialize the UI with the user information
        initializeUI();
    }

    @Override
    protected void initializeUI() {
        if (session != null) {
            customer = session.getCustomer();
            order = session.getActiveOrder();
            availPointsLabel.setText(String.valueOf(customer.getPointsCollected()));
            totalPriceLabel.setText(String.valueOf(order.getTotalPrice()));
        }
    }

    /*
     * Handles the redeem button click event.
     */
    public void redeemBtnOnAction(ActionEvent event) {
        if (validator.isValidPoint(redeemPointTF.getText(), customer.getPointsCollected())){
            pointsToRedeem = Integer.parseInt(redeemPointTF.getText());
            setAndSavePointsUsed();
            applyPoints(event);
        } else{
            messageLabel.setText("The number of point is not valid.");
        }
    }

    /*
     * Applies the redeemed points and updates the UI accordingly.
     */
    private void applyPoints(ActionEvent event){
        double discount = pointsToRedeem / 100.0;
        double orderTotal = order.getTotalPrice() - discount;
        int pointsLeft = customer.getPointsCollected() - pointsToRedeem;

        // Create an alert to display the information about applied points
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Points Applied");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText("Points applied successfully!\n" +
                "Discount: $" + discount + "\n" +
                "New Order Total: $" + orderTotal + "\n" +
                "Remaining Points: " + pointsLeft);

        ButtonType okButton = new ButtonType("OK");
        infoAlert.getButtonTypes().setAll(okButton);

        // Show the alert and wait for the user's response
        infoAlert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                try {
                    updateUserSessionOrders();
                    switchSceneController.dashboard(event, session);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /*
     * Sets and saves the points used in the order to the database and
     * order object.
     */
    public void setAndSavePointsUsed(){
        OrdersDb ordersDB = new OrdersDb();
        order.setPointsUsed(Integer.parseInt(redeemPointTF.getText()));
        ordersDB.updatePointsUsed(order.getOrderID(), order.getPointsUsed());
    }

    /*
    * Update the order in current session and also remaining fries
    * */
    private void updateUserSessionOrders(){
        session.updateRemainingServes(order);
        System.out.println("Remaining fries: " + session.getRemainedFries());
        session.setupOrderHistory(order.getUsername());
        session.resetActiveOrder();
    }
}
