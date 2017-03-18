/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DatabaseManager implements Runnable {
    private static final String TABLE_NAME = "CONNECT_FOUR";

    private SQLDatabase database;
    private String primaryKeyID;

    public DatabaseManager(String primaryKeyID, Saveable saveable) {
        this.primaryKeyID = primaryKeyID;

        database = new SQLDatabase();
        database.addSaveableObject(saveable);
        
        Thread createThread = new Thread(this);
        createThread.setDaemon(true);
        createThread.start();
    }

    public void create() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            String createStatementSQL = database.formatSQLCreate(TABLE_NAME);
            System.out.println(createStatementSQL);

            PreparedStatement statement = connection.prepareStatement(createStatementSQL);
            statement.execute();
            statement.close();

            connection.close();
            System.out.println("[Database] Successfully created table if it didn't exist before.");
        } catch (SQLException e) {
            System.out.println("[Database] Error Creating Table. ");
            e.printStackTrace();
        }
    }

    public void insertOrUpdate() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db");

            String updateSQL = database.formatSQLUpdate(TABLE_NAME, primaryKeyID);
            System.out.println("[Database] insertOrUpdate() query= " + updateSQL);

            PreparedStatement statement = connection.prepareStatement(updateSQL);

            statement.execute();
            statement.close();

            connection.close();
            System.out.println("[Database] Inserted/Updated Successfully.");
        } catch (SQLException e) {
            System.out.println("[Database] Error while inserting or updating.");
            return;
        }
    }

    @Override
    public void run() {
        create();
    }
}