package org.anneelv.burritokingv2.Models;

public class Fries extends FoodItem implements Cookable{
    private static final String name = "Fries";
    private static final int prepTimeForOneServe = 8;
    private static final int batchSize = 5;
    private static final double price = 4.0;

    public Fries(int quantity) {
        super(name, price, quantity);
    }

    private int getActualQuantityNeeded(int remainingServes) {
        if(remainingServes >= this.getQuantity()) {
            return 0;
        }else if(remainingServes > 0){
            return this.getQuantity() - remainingServes;
        }else {
            return this.getQuantity();
        }
    }

    private int getActualBatchesCooked(int remainingServes) {
        return (int) Math.ceil(getActualQuantityNeeded(remainingServes) / ((double) batchSize));
    }

    /*
    * Calculate how many batches needed to fulfill the current order
    * while also take into account the remaining fries available
    * */
    @Override
    public int getActualQuantityCooked(UserSession session) {
        return getActualBatchesCooked(session.getRemainedFries()) * batchSize;
    }

    /**
     * Fries can be cooked in a batch of 5.
     * Therefore, we first compute the number of batches needed.
     * And then multiple it by the prep time for each batch
     */
    @Override
    public int getPreparationTime(UserSession session) {
        return prepTimeForOneServe * getActualBatchesCooked(session.getRemainedFries());
    }
}
