/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.connectFour;

import edu.utep.cs3350.connect4.perezJose.database.Database;
import edu.utep.cs3350.connect4.perezJose.database.Saveable;

/**
 * A game of ConnectFour
 */
public class ConnectFour implements Saveable {
    private Board board;

    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public char tokenAt(int row, int column) {
        return board.tokenAt(row, column);
    }
    
    public String getBoard(){
        return board.toString();
    }

    public int getRows() {
        return board.getRows();
    }

    public int getColumns() {
        return board.getColumns();
    }

    private TurnManager turnManager;

    public Player getCurrentPlayer() {
        return turnManager.getCurrentPlayer();
    }

    private Player playerLocal;

    public Player getPlayerLocal() {
        return playerLocal;
    }

    private Player playerOpponent;

    public Player getPlayerOpponent() {
        return playerOpponent;
    }

    public boolean isLocalTurn() {
        return getCurrentPlayer().equals(getPlayerLocal());
    }

    public ConnectFour(ConnectFour oldGame, boolean localStarting) {
        this(oldGame.getPlayerLocal(), oldGame.getPlayerOpponent(), oldGame.getRows(), oldGame.getColumns(), localStarting);
    }

    public ConnectFour(Player playerLocal, Player playerOpponent, int rows, int columns, boolean localStarting) {
        this(playerLocal, playerOpponent, rows, columns);
        turnManager = new TurnManager(playerLocal, playerOpponent, localStarting);
    }

    public ConnectFour(Player playerLocal, Player playerOpponent, int rows, int columns) {
        this.playerLocal = playerLocal;
        this.playerOpponent = playerOpponent;
        board = new Board(rows, columns);
        turnManager = new TurnManager(playerLocal, playerOpponent);
    }

    public PlacementResult placeToken(int column) {
        if (!isActive())
            return PlacementResult.GAMESTOPPED;

        PlacementResult result = board.placeToken(turnManager.getCurrentPlayer().getToken(), column);

        if (result == PlacementResult.GOOD)
            turnManager.endTurn();

        if (result == PlacementResult.TIE || result == PlacementResult.WIN)
            stop();

        return result;
    }

    public void stop() {
        isActive = false;
    }

    public void start(){
        isActive = true;
    }
    
    public void restart() {
        board.restart(board.getRows(), board.getColumns());
        start();
    }

    public void forfeit() {
        if (!isActive)
            return;

        stop();
        turnManager.endTurn();
    }
    
    public void forceEndTurn(){
        turnManager.endTurn();
    }
    
    public void forceNextTurn(boolean localStarting){
        turnManager = new TurnManager(playerLocal, playerOpponent, localStarting);
    }

    public void shuffleTurns(){
        turnManager = new TurnManager(playerLocal, playerOpponent);
    }
    
    public void loadBoard(String boardString){
        board.loadFromString(boardString);
    }
    
    @Override
    public void toDatabase(Database database) {
        database.add("CF_ACTIVE", isActive);

        board.toDatabase(database);
        turnManager.toDatabase(database);
    }

    @Override
    public boolean fromDatabase(Database database) {
        /*
         * DatabaseItem activeItem = items.get("CF_ACTIVE");
         * if(activeItem == null || activeItem.getItemType() !=
         * ItemType.BOOLEAN)
         * return false;
         * 
         * active = (Boolean)activeItem.getValue();
         */
        // return board.fromDatabase(items) && turnManager.fromDatabase(items);
        return true;
    }

}
