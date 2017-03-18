/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.screen;

/**
 * The ScreenManager is class that is able to manage different screens in a
 * program in order to bring multiple scenes in the most efficient way possible.
 */
public interface ScreenManager {
    /**
     * Marks the ScreenManager to close the current screen and change to a new screen
     * @param newScreen New Screen to create scene from
     */
    public void changeScreen(Screen newScreen);

    /**
     * Marks the ScreenManager to close the current screen and close the application
     */
    public void quit();
}
