package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 7/11/2016.
 */
public class WinPacket extends Packet {

    private String userName;

    public WinPacket(byte[] data) {
        super(12);
        this.userName = readData(data);
    }

    public WinPacket(String userName) {
        super(12);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public byte[] getData() {
        return ("12" + this.userName).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }
}
