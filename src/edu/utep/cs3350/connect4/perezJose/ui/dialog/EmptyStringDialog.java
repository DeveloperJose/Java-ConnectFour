/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.dialog;

public class EmptyStringDialog extends Dialog {
    public EmptyStringDialog(String stringName) {
        super();

        addLine("Please enter a value for " + stringName);
    }

    @Override
    public int getWidth() {
        return 540;
    }

    @Override
    public int getHeight() {
        return 50;
    }
}