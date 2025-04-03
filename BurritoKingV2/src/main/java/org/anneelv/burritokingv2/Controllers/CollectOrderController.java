package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.anneelv.burritokingv2.Models.FinalizedOrder;
import org.anneelv.burritokingv2.Models.UserSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/*
* Controller class for the CollectOrder Scene and functionalities
* */
public class CollectOrderController extends ControllerInit{
    private UserSession session;
    private FinalizedOrder order;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();
    private final Validator validator = new Validator();

    @FXML
    private DatePicker collectionDate;
    @FXML
    private TextField collectionTime;
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
            order = session.getOrderToCollect();
            System.out.println("Order ID to be collected: " + order.getOrderID());
            setDefaultDate();
            setDefaultTime();
        }
    }

    /*
    * Handles event when back button is clicked
    * */
    public void backBtnOnAction(ActionEvent event) throws IOException {
        switchSceneController.dashboard(event, session);
    }

    /*
    * Handles event when collect button is clicked
    * */
    public void collectBtnOnAction(ActionEvent event){
        if (!collectionTime.getText().isBlank()){
            if (validator.isValidTime(collectionTime.getText())){
                saveOrderToDatabase(event);
            } else{
                errorLabel.setText("Please enter a valid collection time");
            }
        } else {
            errorLabel.setText("Please enter collection time");
        }
    }

    /*
    * Sets default date in the DatePicker to current date
    * */
    private void setDefaultDate(){
        collectionDate.setValue(LocalDate.now());
    }

    /*
    * Sets default time in the TextField to current time
    * */
    private void setDefaultTime(){
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = currentTime.format(timeFormatter);
        collectionTime.setText(formattedTime);
    }

    /*
     * Parses the date from the date picker into a string.
     */
    private String parsingDate(DatePicker placementDate){
        LocalDate placedDate = placementDate.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return placedDate.format(formatter);
    }

    /*
    * Saving the current order to the database. Updates the
    * collection date, time and status
    * */
    private void saveOrderToDatabase(ActionEvent event){
        String collectDate = parsingDate(collectionDate);
        if (order.isLaterThanPlaceDateTime(collectDate, collectionTime.getText())){
            order.saveCollectionDateTime();
            collectionSuccessPopup(event);
        } else{
            errorLabel.setText("Order is not ready yet for collection!");
        }
        System.out.println(order.getOrderID()+ " " + order.getUsername() + " " + order.getWaitingTime()+ " " + order.getPlacedDate() + " " + order.getPlacedTime() + " " + order.getCollectDate()+ " " + order.getCollectTime() + " " + order.getTotalPrice());
    }

    /*
    * Shows a success popup after the order is collected
    * */
    private void collectionSuccessPopup(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Collection");
        alert.setHeaderText(null);
        alert.setContentText("Your order has been successfully collected!");

        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);

        // Show the alert and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == okButton) {
                try {
                    switchSceneController.dashboard(event, session);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
