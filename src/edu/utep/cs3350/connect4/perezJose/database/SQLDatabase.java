/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.database;

import java.util.Iterator;
import java.util.Map.Entry;

class SQLDatabase extends Database {
    
    public SQLDatabase(){
        this(true);
    }
    
    protected SQLDatabase(boolean sortedMap) {
        super(sortedMap);
    }

    private static final String CREATE_SQL_FORMAT = "CREATE TABLE IF NOT EXISTS %s (ID STRING PRIMARY KEY NOT NULL, %s);";
    private static final String UPDATE_SQL_FORMAT = "INSERT OR REPLACE INTO %s (ID, %s) VALUES (%s, %s);";

    public String formatSQLCreate(String tableName) {
        return String.format(CREATE_SQL_FORMAT, tableName, getSQLColumns());
    }

    public String formatSQLUpdate(String tableName, String primaryKey) {
        return String.format(UPDATE_SQL_FORMAT, tableName, getSQLKeys(), "'" + primaryKey + "'", getSQLValues());
    }

    private String getSQLColumns() {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<String, DatabaseItem>> dataIterator = getIterator();

        while (dataIterator.hasNext()) {
            Entry<String, DatabaseItem> currentPair = dataIterator.next();

            String key = currentPair.getKey();
            DatabaseItem value = currentPair.getValue();

            // Format: KEY DATATYPE, KEY DATATYPE

            sb.append(key + "\t" + value.getQueryDataType() + ", ");

            dataIterator.remove();
        }
        return sb.substring(0, sb.length() - 2);
    }

    private String getSQLKeys() {
        StringBuilder sb = new StringBuilder();

        Iterator<String> keyIterator = getKeyIterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            sb.append(key + ", ");

            keyIterator.remove();
        }
        return sb.substring(0, sb.length() - 2);
    }

    private synchronized String getSQLValues() {
        StringBuilder sb = new StringBuilder();

        Iterator<DatabaseItem> valueIterator = getValueIterator();

        while (valueIterator.hasNext()) {
            DatabaseItem databaseItem = valueIterator.next();
            ItemType itemType = databaseItem.getItemType();

            if (itemType == ItemType.STRING || itemType == ItemType.CHAR || itemType == ItemType.BOOLEAN) {
                String sanitized = databaseItem.getValue().toString().replaceAll("'", "''");
                sb.append("'" + sanitized + "', ");
            } else
                sb.append(databaseItem.getValue().toString() + ", ");

            valueIterator.remove();
        }
        return sb.substring(0, sb.length() - 2);
    }

    @SuppressWarnings("unused")
    private String getSQLUpdateDuplicates() {
        StringBuilder sb = new StringBuilder();

        Iterator<String> keyIterator = getKeyIterator();

        while (keyIterator.hasNext()) {
            String key = keyIterator.next();

            sb.append(key + "     =     VALUES(" + key + "), ");

            keyIterator.remove();
        }
        return sb.substring(0, sb.length() - 2);
    }
}
