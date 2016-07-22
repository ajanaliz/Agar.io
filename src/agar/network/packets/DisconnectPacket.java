package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 5/31/2015.
 */
public class DisconnectPacket extends Packet {

    private String userName;

    public DisconnectPacket(byte[] data) {
        super(01);
        this.userName = readData(data);
    }

    public DisconnectPacket(String userName) {
        super(01);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public byte[] getData() {
        return ("01" + this.userName).getBytes();
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
