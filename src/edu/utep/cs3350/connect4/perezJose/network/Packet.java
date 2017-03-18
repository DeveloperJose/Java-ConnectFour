/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import edu.utep.cs3350.connect4.perezJose.database.Database;
import edu.utep.cs3350.connect4.perezJose.database.DatabaseItem;

public class Packet {
    private int ID;
    private Database packetData;

    protected Packet(int ID) {
        super();
        this.ID = ID;
        packetData = new Database(false);
    }

    public int getID() {
        return ID;
    }

    public static Packet readPacket(DataInputStream input) throws IOException {
        int ID = input.readInt();
        System.out.println("RECEIVED PACKET ID: " + ID);
        Packet packet = null;

        if (ID == PacketID.DISCONNECT) {
            packet = createPacketDisconnect();

        } else if (ID == PacketID.CLIENT_HANDSHAKE) {
            String playerName = input.readUTF();
            packet = createPacketClientHandshake(playerName);

        } else if (ID == PacketID.SERVER_HANDSHAKE) {
            String playerName = input.readUTF();
            boolean isFirst = input.readBoolean();
            packet = createPacketServerHandshake(playerName, isFirst);

        } else if (ID == PacketID.READY) {
            packet = createPacketReady();

        } else if (ID == PacketID.PLACETOKEN) {
            int column = input.readInt();
            packet = createPacketPlaceToken(column);

        } else if (ID == PacketID.ENDGAME) {
            int resultIndex = input.readInt();
            EndGameResult result = EndGameResult.values()[resultIndex];
            packet = createPacketEndGame(result);
        } else if (ID == PacketID.RESYNC) {
            String board = input.readUTF();
            boolean isFirst = input.readBoolean();
            packet = createPacketResync(board, isFirst);
        } else {
            packet = new Packet(PacketID.INVALID);
        }

        return packet;
    }

    public static Packet createPacketDisconnect() {
        return new Packet(PacketID.DISCONNECT);
    }

    public static Packet createPacketClientHandshake(String playerName) {
        Packet p = new Packet(PacketID.CLIENT_HANDSHAKE);
        p.add(PacketKey.CLIENT_HANDSHAKE_NAME, playerName);
        return p;
    }

    public static Packet createPacketServerHandshake(String playerName, boolean isFirst) {
        Packet p = new Packet(PacketID.SERVER_HANDSHAKE);
        p.add(PacketKey.SERVER_HANDSHAKE_NAME, playerName);
        p.add(PacketKey.SERVER_HANDSHAKE_ISFIRST, isFirst);
        return p;
    }

    public static Packet createPacketReady() {
        return new Packet(PacketID.READY);
    }

    public static Packet createPacketPlaceToken(int column) {
        Packet p = new Packet(PacketID.PLACETOKEN);
        p.add(PacketKey.PLACETOKEN_COLUMN, column);
        return p;
    }

    public static Packet createPacketEndGame(EndGameResult result) {
        Packet p = new Packet(PacketID.ENDGAME);
        p.add(PacketKey.ENDGAME_RESULT, result.ordinal());
        return p;
    }

    public static Packet createPacketResync(String board, boolean isFirst) {
        Packet p = new Packet(PacketID.RESYNC);
        p.add(PacketKey.RESYNC_BOARD, board);
        p.add(PacketKey.RESYNC_ISFIRST, isFirst);
        return p;
    }

    public void sendPacket(DataOutputStream output) throws IOException {
        output.writeInt(getID());

        Iterator<DatabaseItem> iterator = packetData.getValueIterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next().getValue();

            if (obj instanceof String)
                output.writeUTF(obj.toString());
            else if (obj instanceof Boolean)
                output.writeBoolean((Boolean) obj);
            else if (obj instanceof Integer)
                output.writeInt((Integer) obj);
            else if (obj instanceof Double)
                output.writeDouble((Double) obj);
            else if (obj instanceof Character)
                output.writeChar((Character) obj);
            else
                output.writeUTF(obj.toString());
        }
    }

    public void add(PacketKey packetKey, Object item) {
        packetData.add(packetKey.toString(), item);
    }

    private Object getObject(PacketKey key) {
        return packetData.getObject(key.toString());
    }

    public String getString(PacketKey key) {
        return getObject(key).toString();
    }

    public int getInt(PacketKey key) {
        return (Integer) getObject(key);
    }

    public boolean getBoolean(PacketKey key) {
        return (Boolean) getObject(key);
    }

    public double getDouble(PacketKey key) {
        return (Double) getObject(key);
    }

    public char getChar(PacketKey key) {
        return getString(key).charAt(0);
    }
}