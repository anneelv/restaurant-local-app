package org.anneelv.burritokingv2.Database;

import org.anneelv.burritokingv2.Models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * The class that manages database operations related to the Orders table.
 */
public class OrdersDb extends DbSkeleton{
    private final String TABLE_NAME = "Orders";

    /*
     * Sets up the Orders table in the database if it does not already exist.
     * The table includes columns for orderID, username, status, waiting_time, placed_date, placed_time,
     * collect_date, collect_time, total_price, and points_used.
     * The username references the Customer table.
     */
    @Override
    void setupTable() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                    + "(orderID INTEGER PRIMARY KEY NOT NULL,"
                    + "username REFERENCES Customer (username) MATCH SIMPLE NOT NULL,"
                    + "status VARCHAR(10) DEFAULT 'Await for Collection' NOT NULL,"
                    + "waiting_time INT NOT NULL,"
                    + "placed_date VARCHAR(10) NOT NULL,"
                    + "placed_time VARCHAR(8) NOT NULL,"
                    + "collect_date VARCHAR(10),"
                    + "collect_time VARCHAR(8),"
                    + "total_price DOUBLE NOT NULL,"
                    + "points_used INT DEFAULT 0 NULL)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Checks if the Orders table exists in the database.
     * Prints a message indicating whether the table exists or not.
     */
    @Override
    void checkTableExist() {
        try (Connection con = DatabaseConnection.getConnection()) {

            DatabaseMetaData dbm = con.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);

            if(tables != null) {
                if (tables.next()) {
                    System.out.println("Table " + TABLE_NAME + " exists.");
                }
                else {
                    System.out.println("Table " + TABLE_NAME + " does not exist.");
                }
                tables.close(); // use close method to close ResultSet object
            } else {
                System.out.println("Problem with retrieving database metadata");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Inserts a new order into the Orders table.
     * The order includes username, waiting time, placed date, placed time, and total price.
     * Returns the generated order ID or -1 if the insertion fails.
     */
    public int insertNewOrder(String username, int waitingTime, String placedDate, String placedTime, double totalPrice) {
        String sql = "INSERT INTO " + TABLE_NAME + " (username, waiting_time, placed_date, placed_time, total_price) " +
                " VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, username);
            stmt.setInt(2, waitingTime);
            stmt.setString(3, placedDate);
            stmt.setString(4, placedTime);
            stmt.setDouble(5, totalPrice);

            int result = stmt.executeUpdate();

            if (result == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            if (result == 1) {
                System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    /*
     * Updates the status of an order in the Orders table.
     */
    public void updateOrderStatus(int orderID, String status){
        String sql = "UPDATE " + TABLE_NAME + " SET status = ?" + " WHERE orderID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, status);
            stmt.setInt(2, orderID);

            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("Update to table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Updates the collection date of an order in the Orders table.
     */
    public void updateCollectDate(int orderID, String collectionDate){
        String sql = "UPDATE " + TABLE_NAME + " SET collect_date = ?" + " WHERE orderID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, collectionDate);
            stmt.setInt(2, orderID);

            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("Update to table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Updates the collection time of an order in the Orders table.
     */
    public void updateCollectTime(int orderID, String collectionTime){
        String sql = "UPDATE " + TABLE_NAME + " SET collect_time = ?" + " WHERE orderID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, collectionTime);
            stmt.setInt(2, orderID);

            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("Update to table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Updates the points used for an order in the Orders table.
     */
    public void updatePointsUsed(int orderID, int pointsUsed){
        String sql = "UPDATE " + TABLE_NAME + " SET points_used = ?" + " WHERE orderID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setInt(1, pointsUsed);
            stmt.setInt(2, orderID);

            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("Update to table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Retrieves orders for a specific username from the Orders table.
     */
    public List<FinalizedOrder> retrieveOrders(String username){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
        List<FinalizedOrder> orders = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int orderID = resultSet.getInt("orderID");
                    String status = resultSet.getString("status");
                    int waitingTime = resultSet.getInt("waiting_time");
                    String placedDate = resultSet.getString("placed_date");
                    String placedTime = resultSet.getString("placed_time");
                    double totalPrice = resultSet.getDouble("total_price");
                    int pointsUsed = resultSet.getInt("points_used");

                    FinalizedOrder order = new FinalizedOrder(orderID, username, status, waitingTime, placedDate, placedTime, totalPrice, pointsUsed);
                    order.setOrderItems(retrieveOrderItems(orderID));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve orders: " + e.getMessage());
        }

        return orders;
    }

    /*
     * Retrieves order items for a specific username and order from the Order_Item table.
     */
    private List<FoodItem> retrieveOrderItems(int orderID){
        String ORDER_ITEM_TABLE = "Order_Item";
        String FOOD_ITEM_TABLE = "Food_Item";

        String sql = "SELECT f.food_name, oi.foodQTY FROM " + ORDER_ITEM_TABLE + " oi " + "JOIN " + FOOD_ITEM_TABLE + " f ON oi.foodID = f.foodID WHERE oi.orderID = ?";
        List<FoodItem> items = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, orderID);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    String foodName = resultSet.getString("food_name");
                    int quantity = resultSet.getInt("foodQTY");

                    FoodItem item;
                    switch (foodName.toLowerCase()) {
                        case "burrito":
                            item = new Burrito(quantity);
                            break;
                        case "fries":
                            item = new Fries(quantity);
                            break;
                        case "soda":
                            item = new Soda(quantity);
                            break;
                        case "meal":
                            item = new Meal(quantity);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown food type: " + foodName);
                    }

                    items.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve order items: " + e.getMessage());
        }
        return items;
    }
}
