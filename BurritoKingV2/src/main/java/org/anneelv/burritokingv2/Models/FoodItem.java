package org.anneelv.burritokingv2.Models;

abstract public class FoodItem {
    private String name;
    private double unitPrice;
    private int quantity;
    public FoodItem(String name, double price, int quantity) {
        this.name = name;
        this.unitPrice = price;
        this.quantity = quantity;
    }


    public String getName(){
        return this.name;
    }
    public double getUnitPrice() {
        return this.unitPrice;
    }
    public void setQuantity(int count) {
        this.quantity = count;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public double getTotalPrice() {
        return this.unitPrice * this.quantity;
    }

}
