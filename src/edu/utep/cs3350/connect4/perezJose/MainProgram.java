/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose;

import edu.utep.cs3350.connect4.perezJose.ui.screen.Screen;
import edu.utep.cs3350.connect4.perezJose.ui.screen.ScreenManager;
import edu.utep.cs3350.connect4.perezJose.ui.screen.StartScreen;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainProgram extends Application implements ScreenManager {

    public static void main(String[] args) {
        launch(args);
    }

    private Stage mainStage;
    private Screen currentScreen;

    public MainProgram() {
        setupStage();
    }

    public void setupStage() {
        mainStage = new Stage();
        mainStage.setTitle("ConnectFour");
        mainStage.setResizable(false);

        try {
            mainStage.getIcons().add(new Image("file:icon.png"));
        } catch (IllegalArgumentException e) {

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        changeScreen(new StartScreen());
    }

    @Override
    public void changeScreen(Screen newScreen) {
        if(currentScreen != null)
            currentScreen.onExit();
        
        currentScreen = newScreen;
        mainStage.close();
        mainStage.setScene(newScreen.createScene(this));
        mainStage.show();
    }

    @Override
    public void quit() {
        mainStage.close();
        currentScreen.onExit();
        try {
            stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}