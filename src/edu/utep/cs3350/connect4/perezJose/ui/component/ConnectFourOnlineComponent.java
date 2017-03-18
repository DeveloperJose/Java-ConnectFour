package edu.utep.cs3350.connect4.perezJose.ui.component;

import edu.utep.cs3350.connect4.perezJose.connectFour.PlacementResult;
import edu.utep.cs3350.connect4.perezJose.connectFour.Player;
import edu.utep.cs3350.connect4.perezJose.network.EndGameResult;
import edu.utep.cs3350.connect4.perezJose.network.NetworkListener;
import edu.utep.cs3350.connect4.perezJose.network.Packet;
import edu.utep.cs3350.connect4.perezJose.network.PacketID;
import edu.utep.cs3350.connect4.perezJose.network.PacketKey;
import edu.utep.cs3350.connect4.perezJose.network.PacketManager;

public abstract class ConnectFourOnlineComponent extends ConnectFourComponent implements NetworkListener {
    private PacketManager packetManager;

    protected void sendPacket(Packet p) {
        packetManager.sendPacket(p);
    }

    public ConnectFourOnlineComponent(Player playerLocal, Player playerOpponent, int rows, int columns, PacketManager packetManager) {
        super(playerLocal, playerOpponent, rows, columns);
        this.packetManager = packetManager;
        packetManager.setNetworkListener(this);

        updatePlayerStatus("Waiting for opponent...");
    }

    @Override
    public void onScreenExit() {
        packetManager.close();
    };

    @Override
    public void onColumnSelected(int column) {
        if (getConnectFour().isLocalTurn()) {
            PlacementResult result = getConnectFour().placeToken(column);
            onLocalPlacement(column, result);
            super.onPlacementResult(column, result);
        }
        getBoardView().updateBoard();
    }

    public void onOpponentColumnSelected(int column) {
        if (!getConnectFour().isLocalTurn()) {
            PlacementResult result = getConnectFour().placeToken(column);
            onOpponentPlacement(column, result);
            super.onPlacementResult(column, result);
        }
        getBoardView().updateBoard();
    }

    public void onLocalPlacement(int column, PlacementResult result) {
        if (result != PlacementResult.OUTOFBOUNDS && result != PlacementResult.COLUMNFULL && result != PlacementResult.GAMESTOPPED)
            sendPacket(Packet.createPacketPlaceToken(column));
    }

    public void onOpponentPlacement(int column, PlacementResult result) {

    }

    @Override
    public void forfeit() {
        sendPacket(Packet.createPacketEndGame(EndGameResult.FORFEIT));
        if (!getConnectFour().isLocalTurn())
            getConnectFour().forceEndTurn();

        super.forfeit();
    }

    @Override
    public void onNetworkConnect() {
        updateStatus("Reconnected to opponent.");
    }

    @Override
    public void onNetworkError(Exception e) {
        updateStatus("Connection to your opponent was lost. Attemting to reconnect...");
        stop();
    }

    protected void sendResync() {
        boolean opponentFirst = !getConnectFour().isLocalTurn();
        sendPacket(Packet.createPacketResync(getConnectFour().getBoard(), opponentFirst));
    }

    @Override
    public void onNetworkPacket(Packet p) {
        if (!getConnectFour().isActive())
            return;

        updateStatus("Received data from your opponent.");

        if (p.getID() == PacketID.PLACETOKEN) {
            int column = p.getInt(PacketKey.PLACETOKEN_COLUMN);
            onOpponentColumnSelected(column);
        } else if (p.getID() == PacketID.ENDGAME) {
            int resultIndex = p.getInt(PacketKey.ENDGAME_RESULT);
            EndGameResult result = EndGameResult.values()[resultIndex];
            System.out.println("Received an endgame request: " + result);
            if (result == EndGameResult.FORFEIT)
                onPlayerWin(getConnectFour().getPlayerLocal(), true);
            else if (result == EndGameResult.LOSE)
                onPlayerWin(getConnectFour().getPlayerOpponent(), false);
            else if (result == EndGameResult.WIN)
                onPlayerWin(getConnectFour().getPlayerLocal(), false);
            else if (result == EndGameResult.TIE)
                onPlayerTie();
        } else if (p.getID() == PacketID.RESYNC) {
            String boardString = p.getString(PacketKey.RESYNC_BOARD);
            boolean isFirst = p.getBoolean(PacketKey.RESYNC_ISFIRST);

            getConnectFour().loadBoard(boardString);
            getConnectFour().forceNextTurn(isFirst);
            refresh();
        }

        getBoardView().updateBoard();
    }
}