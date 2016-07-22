package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

import java.awt.*;

/**
 * Created by Ali J on 7/10/2016.
 */
public class FoodPacket extends Packet{


    private double x,y,radius;
    private Color color;

    public FoodPacket(byte[] data) {
        super(04);
        String[] dataArray = readData(data).split(",");
        this.x = Double.parseDouble(dataArray[0]);
        this.y = Double.parseDouble(dataArray[1]);
        this.radius = Double.parseDouble(dataArray[2]);
        this.color = new Color(Integer.parseInt(dataArray[3]),Integer.parseInt(dataArray[4]),Integer.parseInt(dataArray[5]));
    }

    public FoodPacket(double x,double y, double radius ,Color color) {
        super(04);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public byte[] getData() {
        return ("04" + this.x + "," + this.y +"," + this.radius + "," + this.color.getRed() + "," + this.color.getGreen() + "," + this.color.getBlue()).getBytes();
    }


    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
