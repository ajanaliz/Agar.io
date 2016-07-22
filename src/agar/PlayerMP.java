package agar;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by Ali J on 7/4/2016.
 */
public class PlayerMP extends Player implements Serializable {


    private InetAddress ipAddress;
    private int port;
    private String password;

    public PlayerMP(String name, int x, int y, Color color, BufferedImage image, int playerID, InetAddress ipAddress, int port) {
        super(name, x, y, color, image, playerID);
        if (getImage() != null){
            BufferedImage otherImage = this.getImage();
            BufferedImage newImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

            Graphics g = newImage.createGraphics();
            g.drawImage(otherImage, 0, 0, 10, 10, null);
            g.dispose();
            this.setImage(newImage);
        }
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
