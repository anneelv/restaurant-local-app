package org.anneelv.burritokingv2.Models;

public interface Cookable {
    /**
     * The method to calculate the total preparation time of a food item
     */
    public abstract int getPreparationTime(UserSession session);
    /**
     * The method to calculate the actual number of items that need to be cooked
     */
    public abstract int getActualQuantityCooked(UserSession session);
}
