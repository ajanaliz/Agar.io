package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 7/11/2016.
 */
public class HostPacket extends Packet {
    private int mapWidth, mapHeight, maxFood, gearSizeFactor, maxSpeed;

    public HostPacket(byte[] data) {
        super(15);
        String[] dataArray = readData(data).split(",");
        this.mapWidth = Integer.parseInt(dataArray[0]);
        this.mapHeight = Integer.parseInt(dataArray[1]);
        this.maxFood = Integer.parseInt(dataArray[2]);
        this.gearSizeFactor = Integer.parseInt(dataArray[3]);
        this.maxSpeed = Integer.parseInt(dataArray[4]);
    }

    public HostPacket(int mapWidth, int mapHeight, int maxFood, int gearSizeFactor, int maxSpeed) {
        super(15);
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.maxFood = maxFood;
        this.gearSizeFactor = gearSizeFactor;
        this.maxSpeed = maxSpeed;
    }


    @Override
    public byte[] getData() {
        return ("15" + this.mapWidth + "," + this.mapHeight + "," + this.maxFood + "," + this.gearSizeFactor + "," + this.maxSpeed).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getMaxFood() {
        return maxFood;
    }

    public void setMaxFood(int maxFood) {
        this.maxFood = maxFood;
    }

    public int getGearSizeFactor() {
        return gearSizeFactor;
    }

    public void setGearSizeFactor(int gearSizeFactor) {
        this.gearSizeFactor = gearSizeFactor;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
