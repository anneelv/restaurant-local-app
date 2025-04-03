package org.anneelv.burritokingv2.Models;

public class Burrito extends FoodItem implements Cookable{
    private static final String name = "Burrito";
    private static final int batchPrepTime = 9;
    private static final int batchSize = 2;
    private static final double price = 7.0;

    public Burrito(int quantity) {
        super(name, price, quantity);
    }

    /**
     * Burritos can be cooked in a batch of 2.
     * Therefore, we first compute the number of batches needed.
     * And then multiple it by the prep time for each batch
     */
    @Override
    public int getPreparationTime(UserSession session) {
        return batchPrepTime * ((int) Math.ceil(this.getQuantity() / ((double)batchSize)));
    }

    /**
     * Burritos cannot be left to next order.
     * Therefore, we always cook the number of burritos ordered by customer
     */
    @Override
    public int getActualQuantityCooked(UserSession session) {
        return this.getQuantity();
    }
}
