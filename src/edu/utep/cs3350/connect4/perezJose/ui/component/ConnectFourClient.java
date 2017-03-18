package edu.utep.cs3350.connect4.perezJose.ui.component;

import edu.utep.cs3350.connect4.perezJose.connectFour.Player;
import edu.utep.cs3350.connect4.perezJose.network.EndGameResult;
import edu.utep.cs3350.connect4.perezJose.network.Packet;
import edu.utep.cs3350.connect4.perezJose.network.PacketID;
import edu.utep.cs3350.connect4.perezJose.network.PacketKey;
import edu.utep.cs3350.connect4.perezJose.network.PacketManager;

public class ConnectFourClient extends ConnectFourOnlineComponent {

    public ConnectFourClient(Player playerLocal, Player playerOpponent, int rows, int columns, PacketManager packetManager,
            boolean isFirst) {
        super(playerLocal, playerOpponent, rows, columns, packetManager);

        getConnectFour().forceNextTurn(isFirst);
        sendPacket(Packet.createPacketReady());

        start();
    }

    @Override
    public void onNetworkConnect() {
        super.onNetworkConnect();
        sendPacket(Packet.createPacketClientHandshake(getConnectFour().getPlayerLocal().getName()));
    }

    @Override
    public void onNetworkError(Exception e) {
        super.onNetworkError(e);
    }

    @Override
    public void onNetworkPacket(Packet p) {
        super.onNetworkPacket(p);

        if (p.getID() == PacketID.ENDGAME) {
            int resultIndex = p.getInt(PacketKey.ENDGAME_RESULT);
            EndGameResult result = EndGameResult.values()[resultIndex];
            System.out.println("Received an endgame request: " + result);
            if (result == EndGameResult.RESTART) {
                updateStatus("Your opponent restarted the game.");
                sendPacket(Packet.createPacketReady());
                super.restart();
            }
        } else if (p.getID() == PacketID.READY) {
            sendResync();
        } else if (p.getID() == PacketID.SERVER_HANDSHAKE) {
            String opponentName = p.getString(PacketKey.SERVER_HANDSHAKE_NAME);
            boolean isFirst = p.getBoolean(PacketKey.SERVER_HANDSHAKE_ISFIRST);

            getConnectFour().getPlayerOpponent().setName(opponentName);
            getConnectFour().forceNextTurn(isFirst);

            sendPacket(Packet.createPacketReady());

            start();
        }
    }

    @Override
    public boolean restart() {
        updateStatus("Only the host is able to restart.");
        return false;
    }
}
