package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 7/10/2016.
 */
public class DiscoveryPacket extends Packet {
    public DiscoveryPacket(byte[] data) {
        super(03);
    }

    public DiscoveryPacket(String userName) {
        super(03);
    }

    @Override
    public byte[] getData() {
        return ("03").getBytes();
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
