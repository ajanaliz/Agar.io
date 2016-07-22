package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 7/11/2016.
 */
public class PowerUpStartPacketJOINALL extends Packet {


    private String userName;

    public PowerUpStartPacketJOINALL(byte[] data) {
        super(77);
        this.userName = readData(data);
    }

    public PowerUpStartPacketJOINALL(String userName) {
        super(77);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public byte[] getData() {
        return ("77" + this.userName).getBytes();
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
