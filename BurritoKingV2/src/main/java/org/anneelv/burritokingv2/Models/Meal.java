package org.anneelv.burritokingv2.Models;

public class Meal extends FoodItem{
    private static final String name = "Meal";
    private static final double price = 10.5;
    public Meal(int quantity){
        super(name, price, quantity);
    }
}
