package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 8/2/2016.
 */
public class CustomPowerUp extends Packet{
    private String userName;
    private String customPowerUp;

    public CustomPowerUp(byte[] data) {
        super(88);
        String[] dataArray = readData(data).split(",");
        this.userName = dataArray[0];
        this.customPowerUp = dataArray[1];
    }

    public CustomPowerUp(String userName,String customPowerUp) {
        super(88);
        this.userName = userName;
    }
    @Override
    public byte[] getData() {
        return ("88" + this.userName + "," + this.customPowerUp).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomPowerUp() {
        return customPowerUp;
    }

    public void setCustomPowerUp(String customPowerUp) {
        this.customPowerUp = customPowerUp;
    }
}
