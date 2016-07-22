package agar.network.packets;

import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 7/11/2016.
 */
public class SignUpPacket extends Packet {
    private String userName, password;
    private int confirm;

    public SignUpPacket(byte[] data) {
        super(14);
        String[] dataArray = readData(data).split(",");
        this.userName = dataArray[0];
        this.password = dataArray[1];
        this.confirm = Integer.parseInt(dataArray[2]);
    }

    public SignUpPacket(String userName, String password, int confirm) {
        super(14);
        this.userName = userName;
        this.password = password;
        this.confirm = confirm;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public byte[] getData() {
        return ("14" + this.userName + "," + this.password + "," + this.confirm).getBytes();
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public void setUserName(String userName) {
        this.userName = userName;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
