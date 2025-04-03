package org.anneelv.burritokingv2.Models;

import javafx.scene.control.CheckBox;
import org.anneelv.burritokingv2.Database.OrdersDb;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/*
* The FinalizedOrder class keeps track of all past orders that
* are saved to the database. Helps to input the collect date and time
* for order collection
*/
public class FinalizedOrder extends Order implements Comparable<FinalizedOrder>{
    private OrdersDb ordersDB;
    private OrderStatus status;
    private String collectDate;
    private String collectTime;
    private CheckBox select;
    private int pointsCollected;

    /*
    * Make sure to sort the order in the list on descending order
    * */
    @Override
    public int compareTo(FinalizedOrder order) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime thisDateTime = LocalDateTime.parse(this.getPlacedDate() + " " + this.getPlacedTime(), dateTimeFormatter);
        LocalDateTime otherDateTime = LocalDateTime.parse(order.getPlacedDate() + " " + order.getPlacedTime(), dateTimeFormatter);
        return otherDateTime.compareTo(thisDateTime); // Sort in reverse order
    }

    /*
    * Enum for order status
    * */
    public enum OrderStatus {
        AWAIT_FOR_COLLECTION("Await for Collection"),
        CANCELLED("Cancelled"),
        COLLECTED("Collected");

        private final String displayStatus;
        OrderStatus(String displayStatus){
            this.displayStatus = displayStatus;
        }

        @Override
        public String toString() {
            return this.displayStatus;
        }
    }

    public FinalizedOrder(int orderID, String username, String status, int waitingTime, String placedDate, String placedTime, double totalPrice, int pointsUsed) {
        super(orderID, username, waitingTime, placedDate, placedTime, totalPrice, pointsUsed);
        setStatus(status);
        setPointCollected();
        this.select = new CheckBox();
    }

    public OrderStatus getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        switch (status.toLowerCase()) {
            case "await for collection":
                this.status = OrderStatus.AWAIT_FOR_COLLECTION;
                break;
            case "cancelled":
                this.status = OrderStatus.CANCELLED;
                break;
            case "collected":
                this.status = OrderStatus.COLLECTED;
                break;
        }
    }

    public void setCollectDate(String date){
        this.collectDate = date;
    }

    public String getCollectDate(){
        return this.collectDate;
    }

    public void setCollectTime(String time){
        this.collectTime = time;
    }

    public String getCollectTime(){
        return this.collectTime;
    }

    public void setSelect(CheckBox select){
        this.select = select;
    }
    public CheckBox getSelect(){
        return this.select;
    }

    /*
    * Calculate points collected in that order after reduced with the discount
    * that comes from used points.
    * */
    public void setPointCollected() {
        this.pointsCollected = (int)(this.getTotalPrice() - (this.getPointsUsed()/100.0));
    }

    public int getPointsCollected(){
        return this.pointsCollected;
    }

    /*
    * Make sure that the inputted collection date and time is after
    * the placed date and time with addition of the order waiting time.
    * */
    public boolean isLaterThanPlaceDateTime(String collectDate, String collectTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        DateTimeFormatter time_formatter = DateTimeFormatter.ofPattern("HH:mm");
        String collectDateTime = collectDate + " " + collectTime;

        try {
            LocalDateTime placedDateTime = LocalDateTime.parse(this.getPlacedDate() + " " + this.getPlacedTime(), dateTimeFormatter);
            LocalDateTime placedDateTimeWithWaitingTime = placedDateTime.plusMinutes(this.getWaitingTime());

            LocalDateTime collectedDateTime = LocalDateTime.parse(collectDateTime, dateTimeFormatter);

            // Assuming placedDateTime already includes the waiting time
            if (placedDateTimeWithWaitingTime.isAfter(collectedDateTime)) {
                System.out.println("Collected DateTime is before Placed DateTime");
                return false;
            } else {
                // Update the collect date and time if collectedDateTime is later than or equal to placedDateTime
                setCollectDate(collectDate);
                setCollectTime(collectTime);
                return true;
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date/time format. Please use dd-MM-yyyy HH:mm format.", e);
        }
    }

    /*
    * Saving orders that are collected to the database. It saves
    * the order collected date, collected time, and change of status.
    * */
    public void saveCollectionDateTime(){
        ordersDB = new OrdersDb();
        ordersDB.updateCollectDate(this.getOrderID(), this.getCollectDate());
        ordersDB.updateCollectTime(this.getOrderID(), this.getCollectTime());
        ordersDB.updateOrderStatus(this.getOrderID(), "Collected");
    }
}
