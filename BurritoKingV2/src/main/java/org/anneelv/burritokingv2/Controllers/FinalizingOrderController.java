package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.anneelv.burritokingv2.Database.Order_ItemDb;
import org.anneelv.burritokingv2.Models.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/*
 * Controller class for managing fanilizingOrder scene and functionalities.
 */
public class FinalizingOrderController extends ControllerInit {
    private UserSession session;
    private Customer customer;
    private Order order;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();
    private final Validator validator = new Validator();

    @FXML
    private TextField cardNumber;
    @FXML
    private TextField cardCVV;
    @FXML
    private TextField cardExpiryDate;
    @FXML
    private DatePicker placementDate;
    @FXML
    private TextField placementTime;
    @FXML
    private Label errorLabel;

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
            setDefaultDate();
            setDefaultTime();
        }
    }

    /*
    * Handles event when placeOrderButton is clicked
    * */
    public void placeOrderBtnOnAction(ActionEvent event) throws IOException {
        if(isValidInput()){
            if (!validator.isValidCardNumber(cardNumber.getText())){
                errorLabel.setText("Your card number is invalid");
            } else if (!validator.isValidCardCVV(cardCVV.getText())){
                errorLabel.setText("Your card CVV number is invalid");
            } else if (!validator.isActiveCard(cardExpiryDate.getText())){
                errorLabel.setText("Your card is already expired");
            } else if (!validator.isValidTime(placementTime.getText())){
                errorLabel.setText("Please input a proper placement time");
            } else if (validator.isValidCardNumber(cardNumber.getText()) && validator.isValidCardCVV(cardCVV.getText()) && validator.isActiveCard(cardExpiryDate.getText()) && validator.isValidTime(placementTime.getText())){
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Input are all valid");
                order.setPlacedTime(placementTime.getText());
                saveOrderToDatabase();
                if (customer.getStatus().equals("VIP") && (customer.getPointsCollected() >= 50)){
                    pointRedemptionPopup(event);
                } else {
                    updateUserSessionOrders();
                    orderSuccessPopup(event);
                }
            }
        }
    }

    /*
    * Set default date for the DatePicker to current date
    * */
    private void setDefaultDate(){
        placementDate.setValue(LocalDate.now());
    }

    /*
    * Set default time for the textField to current time
    * */
    private void setDefaultTime(){
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = currentTime.format(timeFormatter);
        placementTime.setText(formattedTime);
    }

    /*
    * Validating input fields before proceed to the next step
    * */
    public boolean isValidInput(){
        if(cardNumber.getText().isBlank()){
            errorLabel.setText("Please input your card number");
            return false;
        } else if(cardCVV.getText().isBlank()){
            errorLabel.setText("Please input your card CVV");
            return false;
        } else if(cardExpiryDate.getText().isBlank()){
            errorLabel.setText("Please input your card expiry date");
            return false;
        } else if(placementTime.getText().isBlank()){
            errorLabel.setText("Please input the order placement time");
            return false;
        }else return !cardNumber.getText().isBlank() && !cardCVV.getText().isBlank() && !cardExpiryDate.getText().isBlank() && !placementTime.getText().isBlank();
    }

    /*
     * Parses the date from DatePicker to a string format.
     */
    private String parsingDate(DatePicker placementDate){
        LocalDate placedDate = placementDate.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return placedDate.format(formatter);
    }

    /*
    * Saving the finalized order to the database
    * */
    private void saveOrderToDatabase(){
        String placedDate = parsingDate(placementDate);
        order.setPlacedDate(placedDate);
        order.setTotalPrice();
        order.setWaitingTime(session);
        order.saveOrder();
        System.out.println(order.getOrderID()+ " " + order.getUsername() + " " + order.getWaitingTime()+ " " + order.getPlacedDate() + " " + order.getPlacedTime() + " " + order.getTotalPrice());
        for(FoodItem item: order.getItems()){
            if(item instanceof Burrito){
                System.out.println("Burrito: " + item.getQuantity());
            }
        }
        saveOrderItemToDatabase();
    }

    /*
    * Saving the finalized order's item to teh database
    * */
    private void saveOrderItemToDatabase(){
        Order_ItemDb order_itemDB = new Order_ItemDb();
        int orderID = order.getOrderID();
        int numOfBurritos;
        int numOfFries;
        int numOfSoda;
        int numOfMeal;

        System.out.println("Current order id to collect items: " + orderID);
        System.out.println("Number of items in the order: " + order.getItems().size());

        for(FoodItem item: order.getItems()) {
            if(item instanceof Burrito) {
                if(item.getQuantity() != 0){
                    numOfBurritos = item.getQuantity();
                    order_itemDB.insertNewOrderItem(orderID, item.getName(), numOfBurritos);
                    System.out.println("Burrito: " + numOfBurritos);
                }
            }else if(item instanceof Fries) {
                if (item.getQuantity() != 0){
                    numOfFries = item.getQuantity();
                    order_itemDB.insertNewOrderItem(orderID, item.getName(), numOfFries);
                    System.out.println("Fries: " + numOfFries);
                }
            }else if(item instanceof  Soda){
                if (item.getQuantity() != 0){
                    numOfSoda = item.getQuantity();
                    order_itemDB.insertNewOrderItem(orderID, item.getName(), numOfSoda);
                    System.out.println("Soda: " + numOfSoda);
                }
            }else if (item instanceof Meal) {
                if (item.getQuantity() != 0){
                    numOfMeal = item.getQuantity();
                    order_itemDB.insertNewOrderItem(orderID, item.getName(), numOfMeal);
                    System.out.println("Meal: " + numOfMeal);
                }
            }
        }
        System.out.printf("Adding items to database completed.\n");
    }

    /*
    * Display a popup to inform user that the order is successfully placed
    * */
    private void orderSuccessPopup(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been successfully placed!");

        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);

        // Show the alert and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                // Navigate back to the dashboard
                try {
                    switchSceneController.dashboard(event, session);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /*
    * Update user session for new finalized order and reset the active order
    * */
    private void updateUserSessionOrders(){
        session.updateRemainingServes(order);
        System.out.println("Remaining fries: " + session.getRemainedFries());
        session.setupOrderHistory(order.getUsername());
        session.resetActiveOrder();
    }

    /*
    * Display a popup only for VIP suer to ask if the user wants to redeem
    * their points
    * */
    public void pointRedemptionPopup(ActionEvent event) throws IOException {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "You have " + customer.getPointsCollected() + " points. Do you want to use them?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            switchSceneController.pointRedemption(event, session);
            session.setActiveOrder(order);
        } else {
            updateUserSessionOrders();
            orderSuccessPopup(event);
        }
    }
}
