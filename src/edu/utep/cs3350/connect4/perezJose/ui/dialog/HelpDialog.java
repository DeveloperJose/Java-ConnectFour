/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.dialog;

public class HelpDialog extends Dialog {
    private static HelpDialog instance;
    
    public static HelpDialog getHelpDialog(){
        if(instance == null)
            instance = new HelpDialog();
        
        return instance;
    }
    
    protected HelpDialog(){
        super();
        
        addLine("***** How to Play: *****");
        addLine("2 players play in a 7 row by 8 column board with tokens.");
        addLine("At the beginning of each turn your name and the board will be shown.");
        addLine("Touch a column to drop a token in that column.");
        addLine("After selecting a column the token will be dropped and go to the bottom (gravity)");
        addLine("First player to connect 4 of their tokens in any direction wins");
        addLine("If the board is filled with pieces and no more moves are possible a tie is declared.");
    }
    
    @Override
    public int getWidth() {
        return 540;
    }
    @Override
    public int getHeight() {
        return 225;
    }
}