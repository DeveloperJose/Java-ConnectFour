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
import java.net.ServerSocket;
import java.net.Socket;

public class PacketManager implements Runnable {
    private boolean isActive;
    private String ipAddress;
    private int port;
    private NetworkState networkState;

    public NetworkState getNetworkState() {
        return networkState;
    }

    private Socket clientSocket;

    private DataOutputStream output;
    private DataInputStream input;

    private NetworkListener networkListener;

    public void setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    public synchronized void sendPacket(Packet p) {
        try {
            System.out.println("SENDING PACKET ID: " + p.getID());
            p.sendPacket(output);
        } catch (IOException e) {
            networkListener.onNetworkError(e);
        }
    }

    public synchronized void close() {
        try {
            isActive = false;
            // sendPacket(Packet.createPacketDisconnect());
            clientSocket.close();
        } catch (Exception e) {
            networkListener.onNetworkError(e);
        }
    }

    public PacketManager(NetworkListener networkListener, NetworkState networkState, String ipAddress, int port) {
        this.networkListener = networkListener;
        this.networkState = networkState;
        this.ipAddress = ipAddress;
        this.port = port;

        Thread listeningThread = new Thread(this);
        listeningThread.setDaemon(true);
        listeningThread.start();

        isActive = true;
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                if (networkState == NetworkState.HOST) {
                    ServerSocket serverSocket = new ServerSocket(port);
                    clientSocket = serverSocket.accept();
                    serverSocket.close();
                } else {
                    clientSocket = new Socket(ipAddress, port);
                }

                output = new DataOutputStream(clientSocket.getOutputStream());
                input = new DataInputStream(clientSocket.getInputStream());

                networkListener.onNetworkConnect();

                while (clientSocket.isConnected() && !clientSocket.isClosed()) {
                    Packet readPacket = Packet.readPacket(input);
                    networkListener.onNetworkPacket(readPacket);
                }

                if (output != null)
                    output.close();
                if (input != null)
                    input.close();

            } catch (Exception e) {
                networkListener.onNetworkError(e);
            }
        }
    }
}
