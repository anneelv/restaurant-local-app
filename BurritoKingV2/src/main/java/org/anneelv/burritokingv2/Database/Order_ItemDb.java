package org.anneelv.burritokingv2.Database;

import java.sql.*;

/*
 * The class that manages database operations related to the Order_Item table.
 */
public class Order_ItemDb extends DbSkeleton {
    private final String TABLE_NAME = "Order_Item";

    /*
     * Sets up the Order_Item table in the database if it does not already exist.
     * The table includes columns for orderID, foodID, and foodQTY.
     * orderID references the Orders table and foodID references the Food_Item table.
     */
    @Override
    void setupTable() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                    + "(orderID REFERENCES Orders (orderID) MATCH SIMPLE NOT NULL,"
                    + "foodID REFERENCES Food_Item (foodID) MATCH SIMPLE NOT NULL,"
                    + "foodQTY INT NOT NULL)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Checks if the Order_Item table exists in the database.
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
                tables.close();
            } else {
                System.out.println("Problem with retrieving database metadata");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Inserts a new order item into the Order_Item table.
     * The order item consists of an order ID, a food ID, and a food quantity.
     */
    public void insertNewOrderItem(int orderId, String foodName, int foodQuantity) {
        int foodID = retrieveFoodID(foodName);
        String sql = "INSERT INTO " + TABLE_NAME + " (orderID, foodID, foodQTY) " +
                " VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, foodID);
            stmt.setInt(3, foodQuantity);

            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }
            else {
                System.out.printf("There are several rows detected: %d", result);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Retrieves the food ID for a given food name from the Food_Item table.
     * @return the ID of the food item, or -1 if the food item is not found
     */
    public int retrieveFoodID(String foodName){
        String FOOD_TABLE_NAME = "Food_Item";
        String sql = "SELECT foodID FROM " + FOOD_TABLE_NAME + " WHERE food_name = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, foodName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("foodID");
                } else {
                    // Food not found with the provided name
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

}
