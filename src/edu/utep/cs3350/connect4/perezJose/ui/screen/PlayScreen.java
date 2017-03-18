/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.screen;

import edu.utep.cs3350.connect4.perezJose.connectFour.Player;
import edu.utep.cs3350.connect4.perezJose.database.DatabaseManager;
import edu.utep.cs3350.connect4.perezJose.database.Database;
import edu.utep.cs3350.connect4.perezJose.database.Saveable;
import edu.utep.cs3350.connect4.perezJose.network.NetworkState;
import edu.utep.cs3350.connect4.perezJose.network.PacketManager;
import edu.utep.cs3350.connect4.perezJose.ui.component.ConnectFourClient;
import edu.utep.cs3350.connect4.perezJose.ui.component.ConnectFourComponent;
import edu.utep.cs3350.connect4.perezJose.ui.component.ConnectFourComponentSubscriber;
import edu.utep.cs3350.connect4.perezJose.ui.component.ConnectFourHost;
import edu.utep.cs3350.connect4.perezJose.ui.component.TimerComponent;
import edu.utep.cs3350.connect4.perezJose.ui.component.TimerSubscriber;
import edu.utep.cs3350.connect4.perezJose.ui.dialog.HelpDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PlayScreen extends Screen implements TimerSubscriber, ConnectFourComponentSubscriber, Saveable {
    private static final Color STATUS_COLOR_STROKE = Color.CORNFLOWERBLUE;
    private static final Color STATUS_COLOR_FILL = Color.CORNFLOWERBLUE;
    private static final int STATUS_FONT_SIZE = 14;

    public static final int ROWS = 7;
    public static final int COLUMNS = 8;
    private ConnectFourComponent connectFourComponent;
    private TimerComponent timerComponent;

    private DatabaseManager databaseManager;

    private final Text textStatus;

    public void setStatus(String status, Object... params) {
        textStatus.setText(String.format(status, params));
    }

    private final Button help;
    private final Button forfeit;
    private final Button restart;
    private final Button quit;

    public synchronized ConnectFourComponent getConnectFourComponent(){
        return connectFourComponent;
    }
    
    public PlayScreen(Player playerOne, Player playerTwo, PacketManager packetManager) {
        this(playerOne, playerTwo, packetManager, false);
    }

    public PlayScreen(Player playerOne, Player playerTwo, PacketManager packetManager, boolean isFirst) {
        this(playerOne, playerTwo);

        if (packetManager.getNetworkState() == NetworkState.CLIENT)
            connectFourComponent = new ConnectFourClient(playerOne, playerTwo, ROWS, COLUMNS, packetManager, isFirst);
        else
            connectFourComponent = new ConnectFourHost(playerOne, playerTwo, ROWS, COLUMNS, packetManager);

        setStatus("{You are %s}\tPlaying against %s {IP -> %s}", packetManager.getNetworkState(), playerTwo.getName(),
                playerTwo.getIPAddress());
    }

    public PlayScreen(Player playerOne, Player playerTwo) {
        connectFourComponent = new ConnectFourComponent(playerOne, playerTwo, ROWS, COLUMNS);
        connectFourComponent.setUISubscriber(this);
        timerComponent = new TimerComponent(this);

        databaseManager = new DatabaseManager("ConnectFour", this);

        textStatus = new Text("{Offline/Local Game}");
        textStatus.setFont(Font.font(STATUS_FONT_SIZE));
        textStatus.setStroke(STATUS_COLOR_STROKE);
        textStatus.setFill(STATUS_COLOR_FILL);

        help = new Button("Help");
        forfeit = new Button("Forfeit");
        restart = new Button("Restart");
        quit = new Button("Quit");

        connectFourComponent.start();
    }

    public void setupScene(ScreenManager screenManager) {
        setupHelpButton(screenManager);

        setupForfeitButton();

        setupRestartButton();

        setupQuitButton(screenManager);

        HBox buttonBox = new HBox(30);
        buttonBox.getChildren().add(help);
        buttonBox.getChildren().add(forfeit);
        buttonBox.getChildren().add(restart);
        buttonBox.getChildren().add(quit);

        addComponent(textStatus, 0, 0);
        addComponent(connectFourComponent.getTextPlayerStatus(), 0, 1);

        addComponent(connectFourComponent.getBoardView(), 0, 2, 4, 1);
        addComponent(connectFourComponent.getTextStatus(), 0, 3);
        addComponent(timerComponent.getCurrentTime(), 1, 3);

        addComponent(buttonBox, 0, 4, 4, 1);
    }

    public void onHelpPressed(ScreenManager screenManager) {
        HelpDialog.getHelpDialog().show(screenManager);
    }

    @Override
    public void onConnectFourStart() {
        timerComponent.startTimer();
        forfeit.setDisable(false);
    }

    @Override
    public void onConnectFourStop() {
        timerComponent.stopTimer();
        forfeit.setDisable(true);
    }

    @Override
    public void onConnectFourRestart() {
        timerComponent.restartTimer();
        forfeit.setDisable(false);
    }

    public void onForfeitPressed() {
        connectFourComponent.forfeit();
        forfeit.setDisable(true);
    }

    public void onRestartPressed() {
        boolean allowRestart = connectFourComponent.restart();

        if (allowRestart) {
            timerComponent.restartTimer();
            forfeit.setDisable(false);
        }
    }

    private void setupHelpButton(ScreenManager screenManager) {
        help.setPrefWidth(90);
        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onHelpPressed(screenManager);
            }
        });
    }

    private void setupForfeitButton() {
        forfeit.setPrefWidth(90);
        forfeit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onForfeitPressed();
            }

        });
    }

    private void setupRestartButton() {
        restart.setPrefWidth(90);
        restart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onRestartPressed();
            }

        });
    }

    private void setupQuitButton(ScreenManager screenManager) {
        quit.setPrefWidth(90);
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                screenManager.quit();
            }

        });
    }

    @Override
    public void onExit() {
        connectFourComponent.onScreenExit();
    }

    @Override
    public void toDatabase(Database database) {
        connectFourComponent.toDatabase(database);
        timerComponent.toDatabase(database);
    }

    @Override
    public boolean fromDatabase(Database database) {
        return connectFourComponent.fromDatabase(database) && timerComponent.fromDatabase(database);
    }

    @Override
    public void onElapsedTime(int minutes, int seconds) {
        if (seconds % 10 == 0) {
            databaseManager.insertOrUpdate();
        }
    }

    @Override
    public int getWidth() {
        return 550;
    }

    @Override
    public int getHeight() {
        return 550;
    }
}