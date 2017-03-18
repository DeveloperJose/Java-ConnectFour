package edu.utep.cs3350.connect4.perezJose.ui.component;

import edu.utep.cs3350.connect4.perezJose.connectFour.PlacementResult;
import edu.utep.cs3350.connect4.perezJose.connectFour.Player;
import edu.utep.cs3350.connect4.perezJose.network.EndGameResult;
import edu.utep.cs3350.connect4.perezJose.network.Packet;
import edu.utep.cs3350.connect4.perezJose.network.PacketID;
import edu.utep.cs3350.connect4.perezJose.network.PacketKey;
import edu.utep.cs3350.connect4.perezJose.network.PacketManager;

public class ConnectFourHost extends ConnectFourOnlineComponent {

    public ConnectFourHost(Player playerLocal, Player playerOpponent, int rows, int columns, PacketManager packetManager) {
        super(playerLocal, playerOpponent, rows, columns, packetManager);

        boolean opponentFirst = !getConnectFour().isLocalTurn();
        sendPacket(Packet.createPacketServerHandshake(playerLocal.getName(), opponentFirst));
    }

    public void onLocalPlacement(int column, PlacementResult result) {
        super.onLocalPlacement(column, result);
        if (result == PlacementResult.WIN)
            sendPacket(Packet.createPacketEndGame(EndGameResult.LOSE));
        else if (result == PlacementResult.TIE)
            sendPacket(Packet.createPacketEndGame(EndGameResult.TIE));
    }

    public void onOpponentPlacement(int column, PlacementResult result) {
        super.onOpponentPlacement(column, result);
        if (result == PlacementResult.WIN)
            sendPacket(Packet.createPacketEndGame(EndGameResult.LOSE));
    }

    @Override
    public void onNetworkPacket(Packet p) {
        super.onNetworkPacket(p);

        if (p.getID() == PacketID.READY){
            sendResync();
            start();
        }
        else if (p.getID() == PacketID.CLIENT_HANDSHAKE) {
            String opponentName = p.getString(PacketKey.CLIENT_HANDSHAKE_NAME);
            getConnectFour().getPlayerOpponent().setName(opponentName);
        }
    }

    @Override
    public void onNetworkConnect() {
        super.onNetworkConnect();

        boolean opponentFirst = !getConnectFour().isLocalTurn();
        sendPacket(Packet.createPacketServerHandshake(getConnectFour().getPlayerLocal().getName(), opponentFirst));
    }

    @Override
    public void onNetworkError(Exception e) {
        super.onNetworkError(e);
        updateStatus("Connection to your opponent was lost. Waiting for opponent to reconnect...");
    }

    @Override
    public boolean restart() {
        getConnectFour().shuffleTurns();
        sendPacket(Packet.createPacketEndGame(EndGameResult.RESTART));

        return super.restart();
    }
}
