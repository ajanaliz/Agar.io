package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 6/1/2015.
 */
public class MovePacket extends Packet {//the code for this packet is 02


    private String username;
    private double x, y;


    public MovePacket(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Double.parseDouble(dataArray[1]);
        this.y = Double.parseDouble(dataArray[2]);
    }

    public MovePacket(String userName, double x, double y) {
        super(02);
        this.username = userName;
        this.x = x;
        this.y = y;
    }

    @Override
    public byte[] getData() {
        return ("02" + this.username + "," + this.x + "," + this.y).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}/*this packet will be sent to the server when the entity actually moves--> like in the peasant when the move method is called,inside the method we'll construct the packet and send it to the server*/
