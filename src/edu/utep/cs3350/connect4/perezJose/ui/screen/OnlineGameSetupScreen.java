/**
 * Class: CS 3331, Advanced Object Oriented Programming
 * Instructor: Omar Ochoa
 * Author: Jose G Perez (UTEP ID: 80473954)
 * Contact: <josegperez@mail.com> or <jperez50@miners.utep.edu>
 * Last Date Modified: 5/1/2016
 */
package edu.utep.cs3350.connect4.perezJose.ui.screen;

import java.net.InetAddress;
import java.net.UnknownHostException;

import edu.utep.cs3350.connect4.perezJose.network.NetworkState;
import edu.utep.cs3350.connect4.perezJose.network.Packet;
import edu.utep.cs3350.connect4.perezJose.network.PacketID;
import edu.utep.cs3350.connect4.perezJose.network.PacketKey;
import edu.utep.cs3350.connect4.perezJose.network.PacketManager;
import edu.utep.cs3350.connect4.perezJose.connectFour.Player;
import edu.utep.cs3350.connect4.perezJose.network.NetworkListener;
import edu.utep.cs3350.connect4.perezJose.ui.dialog.EmptyStringDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class OnlineGameSetupScreen extends Screen implements NetworkListener {
    private static final int PORT = 4200;
    private NetworkState currentState;

    private Label textOnlineGame;
    private Label labelIAm;
    private Label playerNameLabel;
    private Label ipLabel;
    private Label statusLabel;

    private TextField playerName;
    private TextField ip;

    private final ComboBox<NetworkState> comboBoxNetwork;
    ObservableList<NetworkState> options = FXCollections.observableArrayList(NetworkState.HOST, NetworkState.CLIENT);

    private final Button actionButton;
    private final Button backButton;

    private PacketManager packetManager;
    private ScreenManager screenManager;

    public OnlineGameSetupScreen() {
        super();

        setupLabels();

        setupTextFields();

        actionButton = new Button();
        actionButton.setVisible(false);

        backButton = new Button("Back to main menu.");

        comboBoxNetwork = new ComboBox<NetworkState>(options);
    }

    private void setupTextFields() {
        playerName = new TextField();
        playerName.setVisible(false);

        ip = new TextField();
        ip.setVisible(false);
    }

    private void setupLabels() {
        textOnlineGame = new Label("Online Game");
        textOnlineGame.setFont(Font.font(24));

        labelIAm = new Label("I am the ");

        playerNameLabel = new Label("My player name is ");
        playerNameLabel.setVisible(false);

        ipLabel = new Label("");
        ipLabel.setVisible(false);

        statusLabel = new Label();
    }

    @Override
    public void setupScene(ScreenManager screenManager) {
        this.screenManager = screenManager;
        comboBoxNetwork.setOnAction((event) -> {
            onComboBoxItemSelected();
        });

        actionButton.setOnAction((event) -> {
            onActionButton();
        });

        backButton.setOnAction((event) -> {
            screenManager.changeScreen(new StartScreen());
        });

        // addComponent(backButton, 0, 0);
        addComponent(textOnlineGame, 1, 1, 3, 1);

        addComponent(labelIAm, 0, 2);

        addComponent(comboBoxNetwork, 1, 2);

        addComponent(playerNameLabel, 0, 3);
        addComponent(playerName, 1, 3);

        addComponent(ipLabel, 0, 4);
        addComponent(ip, 1, 4);

        addComponent(statusLabel, 0, 5);
        addComponent(actionButton, 1, 5);
    }

    private void onComboBoxItemSelected() {
        currentState = comboBoxNetwork.getSelectionModel().getSelectedItem();
        System.out.println("Current Network State: " + currentState);

        if (currentState == NetworkState.CLIENT) {
            onClientSelected();
        } else {
            onHostSelected();
        }

        playerNameLabel.setVisible(true);
        playerName.setVisible(true);
        ipLabel.setVisible(true);
        ip.setVisible(true);
        actionButton.setVisible(true);
    }

    private void onClientSelected() {
        ipLabel.setText("My opponent's IP Address is ");
        ipLabel.setVisible(true);

        ip.setText("");
        ip.setDisable(false);

        actionButton.setText("Connect");
    }

    private void onHostSelected() {
        ipLabel.setText("My IP Address is ");

        ip.setText(getIPAddress());
        ip.setDisable(true);

        actionButton.setText("Host");
    }

    private void onActionButton() {
        if (playerName.getText().trim().equals("")) {
            new EmptyStringDialog("the player name").show(screenManager);
            setDisable(false);
        } else {
            if (currentState == NetworkState.CLIENT) {
                onClientButton();
            } else {
                onHostButton();
            }
        }
    }

    private void onHostButton() {
        statusLabel.setText("Waiting for client to connect...");
        packetManager = new PacketManager(this, currentState, null, PORT);
        setDisable(true);
    }

    private void onClientButton() {
        if (ip.getText().trim().equals("")) {
            new EmptyStringDialog("the IP Address").show(screenManager);
            setDisable(false);
        } else {
            statusLabel.setText("Connecting...");
            packetManager = new PacketManager(this, currentState, ip.getText(), PORT);
            setDisable(true);
        }
    }

    private void setDisable(boolean disable) {
        if (currentState == NetworkState.CLIENT)
            ip.setDisable(disable);

        playerName.setDisable(disable);
        comboBoxNetwork.setDisable(disable);
        statusLabel.setVisible(disable);
        actionButton.setDisable(disable);
    }

    public String getIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "Unable to determine IP. Make sure you are connected to the Internet.";
        }
    }

    public Player getLocalPlayer() {
        return new Player(playerName.getText(), currentState == NetworkState.HOST ? 'X' : 'O', "localhost");
    }

    private PlayScreen nextScreen = null;

    @Override
    public void onNetworkPacket(Packet p) {
        if (p.getID() == PacketID.CLIENT_HANDSHAKE) {
            String opponentName = p.getString(PacketKey.CLIENT_HANDSHAKE_NAME);
            Player opponent = new Player(opponentName, 'O', ip.getText());

            Platform.runLater(() -> {
                statusLabel.setText(opponentName + " connected");
                nextScreen = new PlayScreen(getLocalPlayer(), opponent, packetManager);
                screenManager.changeScreen(nextScreen);
                packetManager.sendPacket(Packet.createPacketReady());
            });

        } else if (p.getID() == PacketID.SERVER_HANDSHAKE) {
            String opponentName = p.getString(PacketKey.SERVER_HANDSHAKE_NAME);
            boolean isFirst = p.getBoolean(PacketKey.SERVER_HANDSHAKE_ISFIRST);

            Player opponent = new Player(opponentName, 'X', ip.getText());

            Platform.runLater(() -> {
                statusLabel.setText("connnected to " + opponentName);
                nextScreen = new PlayScreen(getLocalPlayer(), opponent, packetManager, isFirst);
                screenManager.changeScreen(nextScreen);
            });

        } else if (p.getID() == PacketID.RESYNC) {
            System.out.println("Too early.");
        }
    }

    @Override
    public void onNetworkConnect() {
        Platform.runLater(() -> statusLabel.setText("Established connection. Getting info."));

        if (currentState == NetworkState.CLIENT)
            packetManager.sendPacket(Packet.createPacketClientHandshake(playerName.getText()));
    }

    @Override
    public void onNetworkError(Exception e) {
        Platform.runLater(() -> {
            if (e.getMessage() != null)
                statusLabel.setText("Couldn't connect to opponent: " + e.getMessage());
            else
                statusLabel.setText("Couldn't connect to opponent.");
            
            setDisable(false);

            packetManager.close();
        });

    }

    @Override
    public int getWidth() {
        return 385;
    }

    @Override
    public int getHeight() {
        return 200;
    }
}