/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.database;

public final class DatabaseItem {
    private Object item;
    private ItemType itemType;

    public DatabaseItem(Object value) {
        this.item = value;
        if (value instanceof String)
            itemType = ItemType.STRING;
        else if (value instanceof Integer)
            itemType = ItemType.INTEGER;
        else if (value instanceof Double)
            itemType = ItemType.DOUBLE;
        else if (value instanceof Character)
            itemType = ItemType.CHAR;
        else if (value instanceof Boolean)
            itemType = ItemType.BOOLEAN;
        else
            itemType = ItemType.STRING;
    }

    public Object getValue() {
        return item;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public final String getQueryDataType() {
        switch (itemType) {
            case STRING:
                return "TEXT NOT NULL";
            case INTEGER:
                return "INT NOT NULL";
            case DOUBLE:
                return "REAL NOT NULL";
            case CHAR:
                return "CHAR(16)";
            case BOOLEAN:
                return "TEXT NOT NULL";
            default:
                return "TEXT NOT NULL";
        }
    }

    public String toString() {
        return "[DBItem]{Value=" + getValue() + ", Type=" + getItemType() + "}";
    }
}
