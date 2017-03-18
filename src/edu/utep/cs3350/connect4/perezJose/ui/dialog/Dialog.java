/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import edu.utep.cs3350.connect4.perezJose.ui.screen.Screen;
import edu.utep.cs3350.connect4.perezJose.ui.screen.ScreenManager;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class Dialog extends Screen {

    private final List<Label> dialogText;

    public Dialog() {
        dialogText = new ArrayList<Label>();
    }

    public void addLine(String line) {
        dialogText.add(new Label(line));
    }

    @Override
    public void setupScene(ScreenManager screenManager) {
        for (int row = 0; row < dialogText.size(); row++)
            addComponent(dialogText.get(row), 0, row);

    }

    public void show(ScreenManager screenManager) {
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setScene(createScene(screenManager));
        dialogStage.show();
    }
}
