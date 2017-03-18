/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.connectFour;

/**
 * A regular player
 */
public class Player {

    private String name;
    private char token;
    private String ipAddress;

    public Player(String name, char token, String ipAddress) {
        this.name = name;
        this.token = token;
        this.ipAddress = ipAddress;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public char getToken() {
        return token;
    }

    public String getIPAddress() {
        return ipAddress;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Player) && other.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return getName().hashCode() + getToken() + getIPAddress().hashCode();
    }
}
