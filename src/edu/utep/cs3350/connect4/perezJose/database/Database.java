/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Database {
    private Map<String, DatabaseItem> databaseMap;
    private List<Saveable> saveableObjects;

    public Database(boolean sortedMap) {
        if (sortedMap)
            databaseMap = new TreeMap<String, DatabaseItem>();
        else
            databaseMap = new HashMap<String, DatabaseItem>();

        this.saveableObjects = new ArrayList<Saveable>();
    }

    public void addSaveableObject(Saveable saveable) {
        saveableObjects.add(saveable);
    }

    public void add(String key, Object item) {
        databaseMap.put(key, new DatabaseItem(item));
    }

    public Object getObject(String key) {
        DatabaseItem fetchedItem = databaseMap.get(key);
        return fetchedItem.getValue();
    }

    public Iterator<String> getKeyIterator() {
        update();
        return databaseMap.keySet().iterator();
    }

    public Iterator<DatabaseItem> getValueIterator() {
        update();
        return databaseMap.values().iterator();
    }

    public Iterator<Entry<String, DatabaseItem>> getIterator() {
        update();
        return databaseMap.entrySet().iterator();
    }

    private void update() {
        for (Saveable saveable : saveableObjects) {
            saveable.toDatabase(this);
        }
    }
}