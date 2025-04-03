package org.anneelv.burritokingv2.Database;

import java.sql.*;

/*
 * The class that manages database operations related to the Customer table.
 */
public class CustomerDb extends DbSkeleton{
    private final String TABLE_NAME = "Customer";

    /*
     * Sets up the Customer table in the database if it does not already exist.
     * The table includes columns for username, first name, last name, status, and password.
     */
    @Override
    void setupTable() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                    + "(username VARCHAR(20) NOT NULL,"
                    + "first_name VARCHAR(20) NOT NULL,"
                    + "last_name VARCHAR(20) NOT NULL,"
                    + "status VARCHAR(8) DEFAULT 'Normal' NOT NULL,"
                    + "password VARCHAR(20) NOT NULL,"
                    + "PRIMARY KEY (username))");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Checks if the Customer table exists in the database.
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
     * Inserts a new user into the Customer table.
     */
    public void insertNewUser(String username, String first_name, String last_name, String password) {
        String sql = "INSERT INTO " + TABLE_NAME + " (username, first_name, last_name, password) " +
                " VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, username);
            stmt.setString(2, first_name);
            stmt.setString(3, last_name);
            stmt.setString(4, password);

            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("Insert into table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * Retrieves and returns user information from the Customer table.
     */
    public String retrieveUserInfo(String username) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
        String result;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            result = (resultSet.getString("username") + " " + resultSet.getString("first_name") + " " +
                    resultSet.getString("last_name") + " " + resultSet.getString("status") + " " +
                    resultSet.getString("password"));
            System.out.printf("Username: %s | First Name: %s | Last Name: %s | Status: %s | Password: %s\n",
                    resultSet.getString("username"), resultSet.getString("first_name"),
                    resultSet.getString("last_name"), resultSet.getString("status"),
                    resultSet.getString("password"));
            System.out.println("Content from result: " + result);
            return result;

        } catch (SQLException e) {
            System.out.println("Fail retrieve user info: :" + e.getMessage());
        }
        return "Failed to retrieve info";
    }

    /*
     * Validates user login by checking if the username and password match an existing user.
     */
    public boolean isValidUser(String username, String password){
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                System.out.println("Username count: " + count);
                return count == 1;
            }
            resultSet.close();

        } catch (SQLException e) {
            System.out.println("Error checking user login:" + e.getMessage());
        }
        return false;
    }

    /*
     * Checks if a username is unique in the Customer table.
     */
    public boolean isUsernameUnique(String username){
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                System.out.println("The username count: " + count);
                resultSet.close();
                return count == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking username uniqueness: " + e.getMessage());
        }
        return false;
    }

    /*
     * Updates the first name of a user in the Customer table.
     */
    public void updateFirstname(String username, String firstname) {
        String sql = "UPDATE " + TABLE_NAME + " SET first_name = ?" + " WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, firstname);
            stmt.setString(2, username);

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
     * Updates the last name of a user in the Customer table.
     */
    public void updateLastname(String username, String lastname){
        String sql = "UPDATE " + TABLE_NAME + " SET last_name = ? WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, lastname);
            stmt.setString(2, username);

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
     * Updates the password of a user in the Customer table.
     */
    public void updatePassword(String username, String password) {
        String sql = "UPDATE " + TABLE_NAME + " SET password = ? WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, password);
            stmt.setString(2, username);

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
    * Updates the status of a user in the Customer table.
    */
    public void updateStatus(String username) {
        String sql = "UPDATE " + TABLE_NAME + " SET status = ? WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1, "VIP");
            stmt.setString(2, username);

            int result = stmt.executeUpdate();

            if (result == 1) {
                System.out.println("Update to table " + TABLE_NAME + " executed successfully");
                System.out.println(result + " row(s) affected");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
