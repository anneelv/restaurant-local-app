package org.anneelv.burritokingv2.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*This unit test was done by commenting the CheckBox variable in
* FinalizedOrder so that we can see if he method is working properly or not*/

class UserSessionTest {
    private UserSession session;
    private Customer customer;
    private FinalizedOrder order;
    private List<FinalizedOrder> pastOrders;

    @BeforeEach
    public void setUp(){
        System.out.println("Setting up the user session class first...");
        session = new UserSession();
        customer = new Customer("user", "first", "last", "VIP", "a");
        session.setCustomer(customer);
    }

    @Test
    public void testSetCollectedPoint_VIPCustomer() {
        FinalizedOrder order4 = new FinalizedOrder(4, "user2", "Collected", 15, "2024-05-30", "12:00", 125.0, 0);
        FinalizedOrder order5 = new FinalizedOrder(5, "user2", "Collected", 15, "2024-05-30", "12:00", 185.0, 0);
        FinalizedOrder order1 = new FinalizedOrder(1, "user1", "Await for Collection", 10, "2024-05-31", "10:00", 50.0, 50);
        FinalizedOrder order2 = new FinalizedOrder(2, "user2", "Collected", 15, "2024-05-30", "12:00", 100.0, 150);
        FinalizedOrder order3 = new FinalizedOrder(3, "user3", "Cancelled", 5, "2024-06-01", "14:00", 80.0, 200);

        pastOrders = new ArrayList<>();
        pastOrders.add(order1);
        pastOrders.add(order2);
        pastOrders.add(order3);
        pastOrders.add(order4);
        pastOrders.add(order5);
        session.setOrderHistory(pastOrders);

        session.setCollectedPoint();
        assertEquals(208, customer.getPointsCollected(), "Points collected should be 50 for VIP customer");
    }

    @Test
    public void testSetCollectedPoint_NonVIPCustomer() {FinalizedOrder order4 = new FinalizedOrder(4, "user2", "Collected", 15, "2024-05-30", "12:00", 125.0, 0);
        FinalizedOrder order5 = new FinalizedOrder(5, "user2", "Collected", 15, "2024-05-30", "12:00", 185.0, 0);
        FinalizedOrder order1 = new FinalizedOrder(1, "user1", "Await for Collection", 10, "2024-05-31", "10:00", 50.0, 50);
        FinalizedOrder order2 = new FinalizedOrder(2, "user2", "Collected", 15, "2024-05-30", "12:00", 100.0, 150);
        FinalizedOrder order3 = new FinalizedOrder(3, "user3", "Cancelled", 5, "2024-06-01", "14:00", 80.0, 200);

        pastOrders = new ArrayList<>();
        pastOrders.add(order1);
        pastOrders.add(order2);
        pastOrders.add(order3);
        pastOrders.add(order4);
        pastOrders.add(order5);
        session.setOrderHistory(pastOrders);


        customer.setStatus("Normal");
        session.setCollectedPoint();
        assertEquals(0, customer.getPointsCollected(), "Points collected should be 0 for non-VIP customer");
    }
}