package agar.network.packets;

import agar.Player;
import agar.network.GameClient;
import agar.network.GameServer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ali J on 5/24/2015.
 */
public class LoginPacket extends Packet {

    private String userName, password;
    private Color color;
    private int playerID, imageWidth, imageHeight;
    private BufferedImage image;
    private double velX, velY;

    private double x, y;

    /*we know that the packet is of Type Login so we're going to send the id manually*/
    public LoginPacket(byte[] data) {
        super(00);
        String[] imagearray = readData(data).split(";");
        String[] dataArray = imagearray[0].split(",");
        this.userName = dataArray[0];
        this.color = new Color(Integer.parseInt(dataArray[1]), Integer.parseInt(dataArray[2]), Integer.parseInt(dataArray[3]));
        this.playerID = Integer.parseInt(dataArray[4]);
        System.out.println(dataArray[5]);
        this.imageWidth = Integer.parseInt(dataArray[5]);
        this.imageHeight = Integer.parseInt(dataArray[6]);
        this.x = Double.parseDouble(dataArray[7]);
        this.y = Double.parseDouble(dataArray[8]);
        this.velX = Double.parseDouble(dataArray[9]);
        this.velY = Double.parseDouble(dataArray[10]);
        this.password = dataArray[11];
        if (imagearray[1].equals("null")) {

        } else {
            this.image = convertStringtoImage(imagearray[1], this.imageWidth, this.imageHeight);
        }
    }/*this is going to be when we're retreiving the data from the packet on the server's side*/


    public LoginPacket(String userName, Color color, int playerID, BufferedImage image, double x, double y, double velX, double velY, String password) {
        super(00);
        this.userName = userName;
        this.color = color;
        this.playerID = playerID;
        this.image = image;
        if (image != null) {
            this.imageWidth = image.getWidth();
            this.imageHeight = image.getHeight();
        }
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.password = password;

    }/*this constructor is going to be used for when we're sending the packet from the client in the original instance when we're creating the packet*/

    public static BufferedImage convertStringtoImage(String imageString, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, 3);
        String[] colors = imageString.split(",");
        System.out.println("its size: " + colors.length + "my size: " + (width * height));
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, Integer.parseInt(colors[width * i + j]));
            }
        }
        return image;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public byte[] getData() {
        if (image != null) {
            return ("00" + this.userName + "," + this.color.getRed() + "," + this.color.getGreen() + "," + this.color.getBlue() + "," + this.playerID + "," + this.imageWidth + "," + this.imageHeight
                    + "," + this.x + "," + this.y + "," + this.velX + "," + this.velY + "," + this.password + ";" +
                    Player.imagetoString(Player.convertTo2DWithoutUsingGetRGB(this.image), this.image.getWidth(), this.image.getHeight())).getBytes();
        } else {
            return ("00" + this.userName + "," + this.color.getRed() + "," + this.color.getGreen() + "," + this.color.getBlue() + "," + this.playerID + "," + 0 + "," + 0 + "," + this.x + "," + this.y
                    + "," + this.velX + "," + this.velY + "," + this.password + ";" + "null").getBytes();
        }
    }/*so now we're going to be actually sending this string of text and this is so that we dont have to create this multiple times in the writeData functions
    ..we can just change it once and we'll be good---> in the write data functions i mean*/

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }
}
