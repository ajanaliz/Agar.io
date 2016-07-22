package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

import java.awt.*;

/**
 * Created by Ali J on 7/10/2016.
 */
public class PowerUpPacket extends Packet {


    private double x,y;
    private int id;

    public PowerUpPacket(byte[] data) {
        super(06);
        String[] dataArray = readData(data).split(",");
        System.out.println(readData(data));
        this.x = Double.parseDouble(dataArray[0]);
        this.y = Double.parseDouble(dataArray[1]);
        this.id = Integer.parseInt(dataArray[2]);
    }

    public PowerUpPacket(double x,double y, int id) {
        super(06);
        this.x = x;
        this.y = y;
        this.id = id;
    }


    @Override
    public byte[] getData() {
        return ("06" + this.x + "," + this.y +"," + this.id).getBytes();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
