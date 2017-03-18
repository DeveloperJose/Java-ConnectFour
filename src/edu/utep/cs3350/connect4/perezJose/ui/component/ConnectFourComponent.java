/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.component;

import edu.utep.cs3350.connect4.perezJose.connectFour.ConnectFour;
import edu.utep.cs3350.connect4.perezJose.connectFour.PlacementResult;
import edu.utep.cs3350.connect4.perezJose.connectFour.Player;
import edu.utep.cs3350.connect4.perezJose.database.Database;
import edu.utep.cs3350.connect4.perezJose.database.Saveable;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ConnectFourComponent implements BoardViewSubscriber, Saveable {
    private final static Color PLAYER_STATUS_COLOR_STROKE = Color.DARKGREEN;
    private final static Color PLAYER_STATUS_COLOR_FILL = Color.LIGHTGREEN;
    private final static int PLAYER_STATUS_FONT_SIZE = 24;

    private final static Color STATUS_COLOR_STROKE = Color.RED;
    private final static Color STATUS_COLOR_FILL = Color.RED;
    private final static int STATUS_FONT_SIZE = 12;

    private BoardView boardView;
    private ConnectFour connectFour;
    private final Text textStatus;
    private final Text textPlayerStatus;

    private ConnectFourComponentSubscriber subscriberUI;

    public void setUISubscriber(ConnectFourComponentSubscriber subscriberUI) {
        this.subscriberUI = subscriberUI;
    }

    protected synchronized void updateStatus(String newStatus) {
        textStatus.setText(newStatus);
    }

    protected synchronized void updatePlayerStatus(String playerStatus) {
        textPlayerStatus.setText(playerStatus);
    }

    protected String getCurrentPlayerTurnString() {
        return String.format("It is %s's turn {Token -> '%s'}", getConnectFour().getCurrentPlayer().getName(),
                getConnectFour().getCurrentPlayer().getToken());
    }

    protected synchronized ConnectFour getConnectFour() {
        return connectFour;
    }

    public synchronized BoardView getBoardView() {
        return boardView;
    }

    public synchronized Text getTextStatus() {
        return textStatus;
    }

    public synchronized Text getTextPlayerStatus() {
        return textPlayerStatus;
    }
    
    public void refresh(){
        updatePlayerStatus(getCurrentPlayerTurnString());
        boardView.updateBoard();
    }

    public ConnectFourComponent(Player playerLocal, Player playerOpponent, int rows, int columns) {
        this.connectFour = new ConnectFour(playerLocal, playerOpponent, rows, columns);
        boardView = new BoardView(this, connectFour.getRows(), connectFour.getColumns());

        textPlayerStatus = new Text("");
        textPlayerStatus.setFont(Font.font(PLAYER_STATUS_FONT_SIZE));
        textPlayerStatus.setStroke(PLAYER_STATUS_COLOR_STROKE);
        textPlayerStatus.setFill(PLAYER_STATUS_COLOR_FILL);

        textStatus = new Text("~~~~~~~~~~~~~~ Connect 4 by Jose Perez ~~~~~~~~~~~~~~");
        textStatus.setFont(Font.font(STATUS_FONT_SIZE));
        textStatus.setStroke(STATUS_COLOR_STROKE);
        textStatus.setFill(STATUS_COLOR_FILL);

        boardView.updateBoard();
    }

    public void onScreenExit() {

    }

    public void forfeit() {
        getConnectFour().forfeit();
        onPlayerWin(getConnectFour().getCurrentPlayer(), true);
        boardView.updateBoard();
        
        if (subscriberUI != null)
            subscriberUI.onConnectFourStop();
    }

    public void start() {
        getConnectFour().start();
        refresh();

        if (subscriberUI != null)
            subscriberUI.onConnectFourStart();
    }

    public boolean restart() {
        getConnectFour().restart();
        start();
        
        if (subscriberUI != null)
            subscriberUI.onConnectFourRestart();
        
        return true;
    }

    public void stop() {
        getConnectFour().stop();

        if (subscriberUI != null)
            subscriberUI.onConnectFourStop();
    }

    @Override
    public char tokenAt(int row, int column) {
        return getConnectFour().tokenAt(row, column);
    }

    @Override
    public void onColumnSelected(int column) {
        onPlacementResult(column, getConnectFour().placeToken(column));
    }

    public void onPlacementResult(int column, PlacementResult result) {
        Player current = getConnectFour().getCurrentPlayer();
        if (result == PlacementResult.GOOD || result == PlacementResult.COLUMNFULL || result == PlacementResult.OUTOFBOUNDS)
            updatePlayerStatus(getCurrentPlayerTurnString());

        else if (result == PlacementResult.WIN)
            onPlayerWin(current, false);
        else if (result == PlacementResult.TIE)
            onPlayerTie();

        boardView.updateBoard();
    }

    protected void onPlayerTie() {
        updatePlayerStatus("Both players have tied! [Board is full]");
        stop();
    }

    protected void onPlayerWin(Player current, boolean winByForfeit) {
        String extra = (winByForfeit) ? " [Opponent forfeited]" : "";
        updatePlayerStatus(current.getName() + " has won!" + extra);
        stop();
    }

    @Override
    public void toDatabase(Database database) {
        connectFour.toDatabase(database);
        database.add("CF_STATUS", getTextStatus().getText());
    }

    @Override
    public boolean fromDatabase(Database database) {
        return true;
    }

}
