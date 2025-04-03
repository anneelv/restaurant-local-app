package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.anneelv.burritokingv2.Models.*;

import java.io.IOException;

/*
 * Controller class for managing foodMenu scene and functionalities.
 */
public class FoodMenuController extends ControllerInit {
    private UserSession session;
    private Order order;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();
    private int burritoNumber;
    private int friesNumber;
    private int sodaNumber;
    private int mealNumber;

    @FXML
    private Button addMealButton;
    @FXML
    private Button removeMealButton;
    @FXML
    private TextField mealOrderNumberTF;
    @FXML
    private TextField burritoOrderNumberTF;
    @FXML
    private TextField friesOrderNumberTF;
    @FXML
    private TextField sodaOrderNumberTF;

    @Override
    public void initUserSession(UserSession userSession) {
        this.session = userSession;
        this.order = userSession.getActiveOrder();

        // Initialize the UI with the user information
        initializeUI();
    }

    @Override
    protected void initializeUI() {
        if (session != null) {
            // Get the authenticated user from the UserSession
            Customer customer = session.getCustomer();

            // Setup non-editable fields and active order values
            setupNonEditableFields();
            setupActiveOrderValues();

            // Disable meal buttons for normal customers
            if (customer.getStatus().equals("Normal")){
                addMealButton.setDisable(true);
                removeMealButton.setDisable(true);
                mealOrderNumberTF.setDisable(true);
            }
        }
    }

    /*
     * Sets up non-editable text fields.
     */
    private void setupNonEditableFields(){
        burritoOrderNumberTF.setEditable(false);
        friesOrderNumberTF.setEditable(false);
        sodaOrderNumberTF.setEditable(false);
        mealOrderNumberTF.setEditable(false);
    }

    /*
     * Sets up the active order values to the text fields.
     */
    private void setupActiveOrderValues(){
        for(FoodItem item: order.getItems()) {
            if(item instanceof Burrito) {
                burritoNumber += item.getQuantity();
            }else if(item instanceof Fries) {
                friesNumber += item.getQuantity();
            }else if(item instanceof  Soda) {
                sodaNumber += item.getQuantity();
            }else if (item instanceof Meal) {
                mealNumber += item.getQuantity();
            }
        }
        if (burritoNumber > 0){
            burritoOrderNumberTF.setText(Integer.toString(burritoNumber));
        }
        if (friesNumber > 0){
            friesOrderNumberTF.setText(Integer.toString(friesNumber));
        }
        if (sodaNumber > 0){
            sodaOrderNumberTF.setText(Integer.toString(sodaNumber));
        }
        if (mealNumber > 0){
            mealOrderNumberTF.setText(Integer.toString(mealNumber));
        }
    }

    public void addMeal(){
        mealNumber += 1;
        mealOrderNumberTF.setText(Integer.toString(mealNumber));
        order.setFoodItems(new Meal(mealNumber));
    }

    public void removeMeal(){
        if (mealNumber > 0) {
            mealNumber -= 1;
            mealOrderNumberTF.setText(Integer.toString(mealNumber));
            order.setFoodItems(new Meal(mealNumber));
        }
    }

    public void addBurrito(){
        burritoNumber += 1;
        burritoOrderNumberTF.setText(Integer.toString(burritoNumber));
        order.setFoodItems(new Burrito(burritoNumber));
    }

    public void removeBurrito(){
        if (burritoNumber > 0) {
            burritoNumber -= 1;
            burritoOrderNumberTF.setText(Integer.toString(burritoNumber));
            order.setFoodItems(new Burrito(burritoNumber));
        }

    }

    public void addFries(){
        friesNumber += 1;
        friesOrderNumberTF.setText(Integer.toString(friesNumber));
        order.setFoodItems(new Fries(friesNumber));
    }

    public void removeFries(){
        if (friesNumber > 0) {
            friesNumber -= 1;
            friesOrderNumberTF.setText(Integer.toString(friesNumber));
            order.setFoodItems(new Fries(friesNumber));
        }

    }

    public void addSoda(){
        sodaNumber += 1;
        sodaOrderNumberTF.setText(Integer.toString(sodaNumber));
        order.setFoodItems(new Soda(sodaNumber));
    }

    public void removeSoda(){
        if (sodaNumber > 0){
            sodaNumber -= 1;
            sodaOrderNumberTF.setText(Integer.toString(sodaNumber));
            order.setFoodItems(new Soda(sodaNumber));
        }

    }

    /*
     * Event handler for navigating to checkout.
     */
    public void goToCheckout(ActionEvent event) throws IOException {
        switchSceneController.checkout(event, session);
    }
}
