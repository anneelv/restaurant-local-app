package org.anneelv.burritokingv2.Models;

import org.anneelv.burritokingv2.Controllers.UserSessionListener;
import org.anneelv.burritokingv2.Database.CustomerDb;
import org.anneelv.burritokingv2.Database.OrdersDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserSession {
    private CustomerDb customerDB = new CustomerDb();
    private OrdersDb ordersDB;
    private static Customer customer;
    private List<UserSessionListener> listeners = new ArrayList<>();
    private Order activeOrder;
    private List<FinalizedOrder> pastOrders;
    private FinalizedOrder orderToCollect;
    private static int remainingFries;

    public void setupCustomer(String username){
        String custInfo = customerDB.retrieveUserInfo(username.toLowerCase());
        String[] info = custInfo.split(" ");

        String cust_username = info[0];
        String first_name = info[1];
        String last_name = info[2];
        String status = info[3];
        String password = info[4];

        customer = new Customer(cust_username, first_name, last_name, status, password);
        activeOrder = new Order(cust_username);
        pastOrders = new ArrayList<>();
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public Customer getCustomer() {
        System.out.println("Customer first name: " + customer.getFirstname());
        return customer;
    }

    public void updateCustomer(Customer updatedCustomer) {
        this.customer = updatedCustomer;
        updateCustomerDB();
        emitUserSessionEvent();
    }

    public void updateCustomerDB(){
        customerDB.updateFirstname(customer.getUsername(), customer.getFirstname());
        customerDB.updateLastname(customer.getUsername(), customer.getLastname());
        customerDB.updatePassword(customer.getUsername(), customer.getPassword());
    }

    public void addListener(UserSessionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(UserSessionListener listener) {
        listeners.remove(listener);
    }

    public void emitUserSessionEvent() {
        for (UserSessionListener listener : listeners) {
            listener.onUserSessionUpdated(customer);
        }
    }

    public void setActiveOrder(Order order){
        this.activeOrder = order;
    }

    public Order getActiveOrder(){
        return this.activeOrder;
    }

    public void setOrderToCollect(FinalizedOrder order) {
        this.orderToCollect = order;
    }

    public FinalizedOrder getOrderToCollect() {
        return this.orderToCollect;
    }

    public void setupOrderHistory(String username){
        ordersDB = new OrdersDb();
        this.pastOrders = ordersDB.retrieveOrders(username);
        setCollectedPoint();
    }

    public void setOrderHistory(List<FinalizedOrder> order) {
        this.pastOrders = order;
    }

    public List<FinalizedOrder> getOrderHistory(){
        return this.pastOrders;
    }

    public void setCollectedPoint(){
        int pointCount = 0;
        int pointUsedCount = 0;
        int pointsLeft = 0;
        for (FinalizedOrder order : pastOrders) {
            if (order.getStatus().equals(FinalizedOrder.OrderStatus.AWAIT_FOR_COLLECTION)){
                pointUsedCount += order.getPointsUsed();
            }
            if (order.getStatus().equals(FinalizedOrder.OrderStatus.COLLECTED)) {
                order.setPointCollected();
                pointCount += order.getPointsCollected();
                pointUsedCount += order.getPointsUsed();
            }
            System.out.println("Point Count: " + pointCount);
            System.out.println("Points Used: " + pointUsedCount);
        }
        pointsLeft = pointCount - pointUsedCount;
        if (customer.getStatus().equals("VIP")){
            customer.setPointsCollected(pointsLeft);
        }
    }

    public void printOrder() {
        for (FinalizedOrder order : pastOrders) {
            System.out.printf("OrderID: %d | Username: %s | Status: %s | Waiting Time: %d | Placed Date: %s | Placed Time: %s | Total Price: %.2f | Points Used: %d\n",
                    order.getOrderID(), order.getUsername(), order.getStatus(), order.getWaitingTime(),
                    order.getPlacedDate(), order.getPlacedTime(), order.getTotalPrice(), order.getPointsUsed());

            for (FoodItem item : order.getItems()) {
                System.out.printf("\tFood Name: %s | Quantity: %d\n",
                        item.getName(), item.getQuantity());
            }
        }
    }

    public int getRemainedFries(){
        return this.remainingFries;
    }

    public boolean updateRemainingServes(Order order) {
        HashMap<String, Integer> cookables = order.mapToCookables();
        Fries friesToCook = new Fries(cookables.get("Fries"));
        int oldRemained = this.remainingFries;
        this.remainingFries = friesToCook.getActualQuantityCooked(this) + oldRemained - friesToCook.getQuantity();
        if (this.remainingFries != oldRemained) {
            return true;
        }
        return false;
    }

    public void resetActiveOrder(){
        activeOrder = new Order(customer.getUsername());
    }
}
