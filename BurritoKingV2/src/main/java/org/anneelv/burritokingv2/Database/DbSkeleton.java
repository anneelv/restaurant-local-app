package org.anneelv.burritokingv2.Database;

/*
 * Abstract class that defines the basic structure for database operations.
 */
abstract class DbSkeleton {
    /*
     * Sets up the database table.
     * Implementation should include SQL commands to create the table structure.
     */
    abstract void setupTable();

    /*
     * Checks if the database table exists.
     * Implementation should include logic to verify the existence of the table.
     */
    abstract void checkTableExist();
}
