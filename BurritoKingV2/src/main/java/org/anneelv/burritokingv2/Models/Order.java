package org.anneelv.burritokingv2.Models;

import org.anneelv.burritokingv2.Database.OrdersDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
* The Order class keeps track of the user's active user that is not yet finalized
* */
public class Order {
    private OrdersDb order_DB;
    private int orderID;
    private String username;
    // keep tracks of the items that has quantities
    private List<FoodItem> items;
    private double totalPrice;
    private int waitingTime;
    private String placedDate;
    private String placedTime;
    private int pointsUsed;

    /*
    * Normal constructor to hold current active user
    * */
    public Order(String username){
        this.username = username;
        items = new ArrayList<>();
        order_DB = new OrdersDb();
    }

    /*
    * Constructor for finalizedOrder subclass which holds all information
    * retrieved from the database
    * */
    public Order(int orderID, String username, int waitingTime, String placedDate, String placedTime, double totalPrice, int pointsUsed){
        this.orderID = orderID;
        this.username = username;
        this.waitingTime = waitingTime;
        this.placedDate = placedDate;
        this.placedTime = placedTime;
        this.totalPrice = totalPrice;
        this.pointsUsed = pointsUsed;
    }

    /*
    * Add food items to items list
    * */
    public void setFoodItems(FoodItem newItem){
        for(FoodItem item: items) {
            if (item.getClass().getName().equals(newItem.getClass().getName())) {
                item.setQuantity(newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }

    public List<FoodItem> getItems(){
        return items;
    }

    public void setOrderItems(List<FoodItem> foodItems){
        this.items = foodItems;
    }

    public void setPlacedDate(String placedDate){
        this.placedDate = placedDate;
    }

    public void setPlacedTime(String placedTime){
        this.placedTime = placedTime;
    }

    /*
    * Calculate total waiting time for current order
    * */
    public void setWaitingTime(UserSession session){
        HashMap<String, Integer> cookables = this.mapToCookables();
        int cookTimeForBurritos = new Burrito(cookables.get("Burritos")).getPreparationTime(session);
        int cookTimeForFries = new Fries(cookables.get("Fries")).getPreparationTime(session);
        this.waitingTime = Math.max(cookTimeForFries, cookTimeForBurritos);
    }

    /*
    * Calculating total price for current order
    * */
    public void setTotalPrice(){
        double sum = 0.0;
        for(FoodItem item: items) {
            sum += item.getTotalPrice();
        }
        this.totalPrice = sum;
    }

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }

    public String getUsername() {
        return this.username;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public int getWaitingTime(){
        return this.waitingTime;
    }

    public String getPlacedDate(){
        return this.placedDate;
    }

    public String getPlacedTime(){
        return this.placedTime;
    }

    public int getOrderID() {
        return this.orderID;
    }

    public void setPointsUsed(int points){
        this.pointsUsed = points;
    }

    public int getPointsUsed(){
        return this.pointsUsed;
    }

    /*
    * Maps a list of ordered food items to a map containing
    * the number of different items to be cooked.
    * */
    public HashMap<String, Integer> mapToCookables(){
        int numOfBurritos = 0;
        int numOfFries = 0;
        for(FoodItem item: items) {
            if(item instanceof Burrito) {
                numOfBurritos += item.getQuantity();
            }else if(item instanceof Fries) {
                numOfFries += item.getQuantity();
            }else if (item instanceof Meal) {
                numOfBurritos += item.getQuantity();
                numOfFries += item.getQuantity();
            }
        }
        HashMap<String, Integer> mapped = new HashMap<String, Integer>();
        mapped.put("Burritos", numOfBurritos);
        mapped.put("Fries", numOfFries);
        return mapped;
    }

    /*
    * Saving the active order to the database and retrieve the order ID
    * of that order.
    * */
    public void saveOrder(){
        int id = order_DB.insertNewOrder(this.getUsername(), this.getWaitingTime(), this.getPlacedDate(), this.getPlacedTime(), this.getTotalPrice());
        setOrderID(id);
    }
}
