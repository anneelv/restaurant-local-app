package org.anneelv.burritokingv2.Models;

public class Soda extends FoodItem{
    private static final String name = "Soda";
    private static final double price = 2.5;

    public Soda(int quantity) {
        super(name, price, quantity);
    }
}
