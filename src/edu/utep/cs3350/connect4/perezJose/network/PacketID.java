/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.network;

public class PacketID {
    protected PacketID() {

    }

    public static final int DISCONNECT = -1;
    public static final int CLIENT_HANDSHAKE = 0;
    public static final int SERVER_HANDSHAKE = 1;
    public static final int READY = 2;
    public static final int PLACETOKEN = 3;
    public static final int ENDGAME = 4;
    public static final int RESYNC = 5;

    public static final int INVALID = Integer.MIN_VALUE;
}
