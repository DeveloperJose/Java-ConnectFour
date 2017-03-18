/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.screen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StartScreen extends Screen {
    private final Text textSceneTitle;

    private final Button buttonLocalGame;
    private final Button buttonOnlineGame;
    private final VBox buttonBox;

    public StartScreen() {
        super();
        textSceneTitle = new Text("Welcome to Connect 4");
        textSceneTitle.setFont(Font.font(16));

        buttonLocalGame = new Button("Local Game");
        buttonOnlineGame = new Button("Online Game");

        buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(buttonLocalGame);
        buttonBox.getChildren().add(buttonOnlineGame);
    }

    public void setupScene(ScreenManager screenManager) {
        buttonLocalGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onLocalGamePressed(screenManager);
            }
        });

        buttonOnlineGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onOnlineGamePressed(screenManager);
            }
        });

        addComponent(textSceneTitle, 0, 0, 2, 1);
        addComponent(buttonBox, 1, 1);
    }

    private void onLocalGamePressed(ScreenManager screenManager) {
        screenManager.changeScreen(new LocalGameSetupScreen());
    }

    private void onOnlineGamePressed(ScreenManager screenManager) {
        screenManager.changeScreen(new OnlineGameSetupScreen());
    }

    @Override
    public int getWidth() {
        return 225;
    }

    @Override
    public int getHeight() {
        return 100;
    }
}