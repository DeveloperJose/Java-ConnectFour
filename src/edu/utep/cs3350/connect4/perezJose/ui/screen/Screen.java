/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.screen;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

/**
 * A screen represents a single form or window in the application
 */
public abstract class Screen {
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public abstract void setupScene(ScreenManager screenManager);
    
    private final GridPane gridPane;

    public Screen() {
        gridPane = new GridPane();
        setupGrid();
    }
        
    public void setupGrid(){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        // top, right, bottom, left
        //gridPane.setPadding(new Insets(65, 5, 65, 5));
    }

    public final void addComponent(Node component, int column, int row) {
        gridPane.add(component, column, row);
    }

    public final void addComponent(Node component, int column, int row, int colSpan, int rowSpan) {
        gridPane.add(component, column, row, colSpan, rowSpan);
    }

    public final GridPane getGrid() {
        return gridPane;
    }

    public final Scene createScene(ScreenManager screenManager) {
        setupScene(screenManager);
        Scene currentScene = new Scene(gridPane, getWidth(), getHeight());
        return currentScene;
    }
    
    public void onExit(){
        
    }
}
