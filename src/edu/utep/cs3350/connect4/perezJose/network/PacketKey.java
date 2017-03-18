/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.network;

public enum PacketKey {
    CLIENT_HANDSHAKE_NAME, SERVER_HANDSHAKE_NAME, SERVER_HANDSHAKE_ISFIRST, PLACETOKEN_COLUMN, ENDGAME_RESULT, 
    RESYNC_BOARD, RESYNC_ISFIRST
}
