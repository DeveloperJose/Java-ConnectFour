/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.dialog;

public class CheckNameDialog extends Dialog {
    public CheckNameDialog(String playerName) {
        super();
        playerName = playerName.trim();

        if (playerName == null || playerName.equals(""))
            addLine("Please provide a name for your player.");

    }

    public CheckNameDialog(String playerOneName, String playerTwoName) {
        super();
        playerOneName = playerOneName.trim();
        playerTwoName = playerTwoName.trim();

        if (playerOneName == null || playerOneName.equals(""))
            addLine("Please provide a name for player one.");

        if (playerTwoName == null || playerTwoName.equals(""))
            addLine("Please provide a name for player two.");
    }

    @Override
    public int getWidth() {
        return 400;
    }

    @Override
    public int getHeight() {
        return 100;
    }
}
