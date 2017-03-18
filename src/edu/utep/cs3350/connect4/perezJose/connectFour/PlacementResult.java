/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.connectFour;

/**
 * One of many results occurring while trying to place a token inside a
 * ConnectFour board
 */
public enum PlacementResult {
    /**
     * Nothing interesting happened. The token was successfully placed in place.
     */
    GOOD,
    /**
     * The player attempted to place a token out of bounds
     */
    OUTOFBOUNDS,
    /**
     * The column the player attempted to drop their token in is already full
     */
    COLUMNFULL,
    /**
     * The player dropped the token and won the game
     */
    WIN,
    /**
     * The board got full after placing the token without any player winning
     */
    TIE,
    /**
     * The game was stopped abruptly
     */
    GAMESTOPPED;
}
