package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 7/10/2016.
 */
public class SawPacket extends Packet {


    private double x,y,radius;


    public SawPacket(byte[] data) {
        super(05);
        String[] dataArray = readData(data).split(",");
        this.x = Double.parseDouble(dataArray[0]);
        this.y = Double.parseDouble(dataArray[1]);
        this.radius = Double.parseDouble(dataArray[2]);
    }

    public SawPacket( double x, double y, double radius) {
        super(05);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public byte[] getData() {
        return ("05" + this.x + "," + this.y + "," + this.radius).getBytes();
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

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
