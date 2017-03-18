/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.screen;

import edu.utep.cs3350.connect4.perezJose.connectFour.Player;
import edu.utep.cs3350.connect4.perezJose.ui.dialog.CheckNameDialog;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class LocalGameSetupScreen extends Screen {
    private final Text promptNames;
    private final Label playerOneLabel;
    private final Label playerTwoLabel;
    private final TextField playerOne;
    private final TextField playerTwo;
    private final Button playButton;
    private final Button backButton;

    public LocalGameSetupScreen() {
        super();
        promptNames = new Text("Please enter the player names");

        playerOneLabel = new Label("Player 1: ");
        playerTwoLabel = new Label("Player 2: ");

        playerOne = new TextField();
        playerOne.setFocusTraversable(true);
        playerTwo = new TextField();
        playerTwo.setFocusTraversable(true);

        backButton = new Button("Back");
        backButton.setPrefWidth(65);
        backButton.setFocusTraversable(true);
        
        playButton = new Button("Play");
        playButton.setPrefWidth(65);
        playButton.setFocusTraversable(true);
    }

    @Override
    public void setupScene(ScreenManager screenManager) {
        EventHandler<KeyEvent> textFieldEvent = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    onPlayButton(screenManager);
            }

        };
        playerOne.setOnKeyPressed(textFieldEvent);
        playerTwo.setOnKeyPressed(textFieldEvent);
        playButton.setOnKeyPressed(textFieldEvent);

        playButton.setOnAction((event) -> {
            onPlayButton(screenManager);
        });

        backButton.setOnAction((event) -> {
           screenManager.changeScreen(new StartScreen()); 
        });
        
        HBox buttonBox = new HBox(40);
        buttonBox.getChildren().add(backButton);
        buttonBox.getChildren().add(playButton);
        
        addComponent(promptNames, 1, 1);
        addComponent(playerOneLabel, 0, 3);
        addComponent(playerTwoLabel, 0, 4);
        addComponent(playerOne, 1, 3);
        addComponent(playerTwo, 1, 4);
        addComponent(buttonBox, 1, 5);
    }

    public void onPlayButton(ScreenManager screenManager) {
        if (hasValidNames())
            screenManager.changeScreen(new PlayScreen(new Player(playerOne.getText(), 'X', "your friend"), new Player(playerTwo.getText(), 'O', "your friend")));
        else
            new CheckNameDialog(playerOne.getText(), playerTwo.getText()).show(screenManager);

    }

    private boolean hasValidNames() {
        return !playerOne.getText().trim().equals("") && !playerTwo.getText().trim().equals("");
    }

    @Override
    public int getWidth() {
        return 300;
    }

    @Override
    public int getHeight() {
        return 135;
    }
}