/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.connectFour;

import java.util.Random;

import edu.utep.cs3350.connect4.perezJose.database.Database;
import edu.utep.cs3350.connect4.perezJose.database.Saveable;

public class TurnManager implements Saveable {
    private Random rand;
    private Player[] players;
    private int currentPlayerIndex = -1;

    public TurnManager(Player playerLocal, Player playerOpponent) {
        players = new Player[] { playerLocal, playerOpponent };
        rand = new Random(System.currentTimeMillis() * System.nanoTime());
        currentPlayerIndex = rand.nextInt(players.length);
    }

    public TurnManager(Player playerLocal, Player playerOpponent, boolean localStarting) {
        this(playerLocal, playerOpponent);
        currentPlayerIndex = (localStarting) ? 0 : 1;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    protected void endTurn() {
        currentPlayerIndex++;

        if (currentPlayerIndex >= players.length)
            currentPlayerIndex = 0;
    }

    @Override
    public void toDatabase(Database database) {
        database.add("TM_PLAYERS", players.length);
        database.add("TM_CURRENT", currentPlayerIndex);
        for (int i = 0; i < players.length; i++) {
            database.add("TM_PLAYER_" + i + "_NAME", players[i].getName());
            database.add("TM_PLAYER_" + i + "_TOKEN", players[i].getToken());
        }
    }

    @Override
    public boolean fromDatabase(Database database) {
        /*
         * }
         * DatabaseItem playersItem = items.get("TM_PLAYERS");
         * if(playersItem == null || playersItem.getItemType() !=
         * ItemType.INTEGER)
         * return false;
         * int playerCount = (Integer)playersItem.getValue();
         * players = new Player[playerCount];
         * 
         * DatabaseItem currentItem = items.get("TM_CURRENT");
         * if(currentItem == null || currentItem.getItemType() !=
         * ItemType.INTEGER)
         * return false;
         * int currentIndex = (Integer)currentItem.getValue();
         * currentPlayerIndex = currentIndex;
         * 
         * for(int i = 0; i < playerCount; i++){
         * DatabaseItem currentNameItem = items.get("TM_PLAYER_" + i + "_NAME");
         * if(currentNameItem == null || currentNameItem.getItemType() !=
         * ItemType.STRING)
         * return false;
         * String currentName = currentNameItem.getValue().toString();
         * 
         * DatabaseItem currentTokenItem = items.get("TM_PLAYER_" + i +
         * "_NAME");
         * if(currentTokenItem == null || currentTokenItem.getItemType() !=
         * ItemType.INTEGER)
         * return false;
         * char currentToken = (Character)currentTokenItem.getValue();
         * 
         * players[i] = new Player(currentName, currentToken);
         * }
         */
        return true;
    }
}
