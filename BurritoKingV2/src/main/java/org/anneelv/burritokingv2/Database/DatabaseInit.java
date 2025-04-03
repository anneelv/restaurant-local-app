package org.anneelv.burritokingv2.Database;

/*
 * The class responsible for initializing the database and setting up necessary tables and initial data.
 */
public class DatabaseInit {
    /*
     * Initializes the database by setting up necessary tables and inserting initial data.
     */
    public static void init() {
        CustomerDb customerDB = new CustomerDb();
        OrdersDb ordersDB = new OrdersDb();
        Order_ItemDb order_itemDB = new Order_ItemDb();
        Food_ItemDb food_itemDB = new Food_ItemDb();


        customerDB.checkTableExist();
        customerDB.setupTable();

        ordersDB.checkTableExist();
        ordersDB.setupTable();

        food_itemDB.checkTableExist();
        food_itemDB.setupTable();
        setupItem(food_itemDB);

        order_itemDB.checkTableExist();
        order_itemDB.setupTable();
    }

    /*
    * Inserts initial food items into the Food_Item table.
    * If an item already exists, the insertion is ignored.
    */
    private static void setupItem(Food_ItemDb food_itemDb) {
        food_itemDb.insertNewFood("Burrito", 7.0, 9);
        food_itemDb.insertNewFood("Fries", 4.0, 8);
        food_itemDb.insertNewFood("Soda", 2.5, 0);
        food_itemDb.insertNewFood("Meal", 10.5, 9);
    }

}
