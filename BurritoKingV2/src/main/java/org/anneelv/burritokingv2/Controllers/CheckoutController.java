package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.anneelv.burritokingv2.Models.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

/*
* Controller class for managing Checkout scene and functionalities
* */
public class CheckoutController extends ControllerInit{
    private UserSession session;
    private Customer customer;
    private Order order;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();

    @FXML
    private ImageView mealImage;
    @FXML
    private Label mealNameLabel;
    @FXML
    private Label mealQuantityLabel;
    @FXML
    private Label mealPriceLabel;
    @FXML
    private Label burritoQuantityLabel;
    @FXML
    private Label burritoPriceLabel;
    @FXML
    private Label friesQuantityLabel;
    @FXML
    private Label friesPriceLabel;
    @FXML
    private Label sodaQuantityLabel;
    @FXML
    private Label sodaPriceLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label waitingTimeLabel;

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
            setMealVisibility();
            setItemsQuantity();
            setItemsPrice();
            setWaitingTime();
        }
    }

    /*
    * Handles event when back button is clicked
    * */
    public void backBtnOnAction(ActionEvent event) throws IOException {
        switchSceneController.dashboard(event, session);
    }

    /*
    * Handles event when checkout button is clicked
    * */
    public void checkoutBtnOnAction(ActionEvent event) throws IOException {
        checkoutPopupConfirmation(event);
    }

    /*
     * Sets the visibility of the meal section based on customer status.
     */
    private void setMealVisibility(){
        if (customer.getStatus().equals("Normal")){
            mealImage.setVisible(false);
            mealNameLabel.setVisible(false);
            mealPriceLabel.setVisible(false);
            mealQuantityLabel.setVisible(false);
        }
    }

    /*
     * Sets the quantity labels for each item.
     */
    private void setItemsQuantity(){
        int burritoNumber = 0;
        int friesNumber = 0;
        int sodaNumber = 0;
        int mealNumber = 0;

        for(FoodItem item: order.getItems()) {
            if(item instanceof Burrito) {
                burritoNumber = item.getQuantity();
            }else if(item instanceof Fries) {
                friesNumber = item.getQuantity();
            }else if(item instanceof  Soda) {
                sodaNumber = item.getQuantity();
            }else if (item instanceof Meal) {
                mealNumber = item.getQuantity();
            }
        }

        burritoQuantityLabel.setText(Integer.toString(burritoNumber));
        friesQuantityLabel.setText(Integer.toString(friesNumber));
        sodaQuantityLabel.setText(Integer.toString(sodaNumber));
        mealQuantityLabel.setText(Integer.toString(mealNumber));
    }

    /*
     * Sets the price labels for each item and calculates the total price.
     */
    private void setItemsPrice(){
        double burritoPrice = 0;
        double friesPrice = 0;
        double sodaPrice = 0;
        double mealPrice = 0;

        for(FoodItem item: order.getItems()) {
            if(item instanceof Burrito) {
                burritoPrice = item.getTotalPrice();
            }else if(item instanceof Fries) {
                friesPrice = item.getTotalPrice();
            }else if(item instanceof  Soda) {
                sodaPrice = item.getTotalPrice();
            }else if (item instanceof Meal) {
                mealPrice = item.getTotalPrice();
            }
        }

        double totalPrice = burritoPrice + friesPrice + sodaPrice + mealPrice;

        burritoPriceLabel.setText(Double.toString(burritoPrice));
        friesPriceLabel.setText(Double.toString(friesPrice));
        sodaPriceLabel.setText(Double.toString(sodaPrice));
        mealPriceLabel.setText(Double.toString(mealPrice));
        totalPriceLabel.setText(Double.toString(totalPrice));
    }

    /*
     * Sets the waiting time label based on the longest cooking time of the items.
     */
    private void setWaitingTime(){
        HashMap<String, Integer> cookables = order.mapToCookables();
        int cookTimeForBurritos = new Burrito(cookables.get("Burritos")).getPreparationTime(session);
        int cookTimeForFries = new Fries(cookables.get("Fries")).getPreparationTime(session);
        int waitingTime = Math.max(cookTimeForFries, cookTimeForBurritos);

        waitingTimeLabel.setText(Integer.toString(waitingTime));
    }

    /*
     * Displays a confirmation dialog for checkout.
     */
    private void checkoutPopupConfirmation(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation Dialog");
        alert.setContentText("Do you want to proceed?");

        // Option handling
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            switchSceneController.finalizingOrder(event, session);
        } else {
            alert.close();
        }
    }
}
