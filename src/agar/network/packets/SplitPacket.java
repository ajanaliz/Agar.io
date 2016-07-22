package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 7/11/2016.
 */
public class SplitPacket extends Packet{

    private String userName;

    public SplitPacket(byte[] data) {
        super(07);
        this.userName = readData(data);
    }

    public SplitPacket(String userName) {
        super(07);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public byte[] getData() {
        return ("07" + this.userName).getBytes();
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
