package org.anneelv.burritokingv2.Database;

import java.sql.*;

/*
 * The class that manages database operations related to the Food_Item table.
 */
public class Food_ItemDb extends DbSkeleton{
    private final String TABLE_NAME = "Food_Item";

    /*
     * Sets up the Food_Item table in the database if it does not already exist.
     * The table includes columns for foodID, food_name, price, and cook_time.
     */
    @Override
    void setupTable() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                    + "(foodID INTEGER PRIMARY KEY NOT NULL,"
                    + "food_name VARCHAR(20) NOT NULL,"
                    + "price DOUBLE NOT NULL,"
                    + "cook_time INT NOT NULL)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Checks if the Food_Item table exists in the database.
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
     * Inserts a new food item into the Food_Item table.
     * If the food item already exists, it does not insert a new record.
     */
    void insertNewFood(String foodName, Double price, int cookTime){
        String sql = "INSERT INTO " + TABLE_NAME + " (food_name, price, cook_time) " +
                " SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM " + TABLE_NAME + " WHERE food_name = ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, foodName);
            stmt.setDouble(2, price);
            stmt.setInt(3, cookTime);
            stmt.setString(4, foodName);

            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
