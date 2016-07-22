package agar.gameobjects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;


public class PowerUp extends Food implements Serializable{
    public PowerUp(double x, double y, ObjectType type, Color color) {
        super(x, y, type, color);
        width = 45;
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    public void render(Graphics2D g) {
        if (ObjectType.GOD_MODE.getObjectTypeID() == type.getObjectTypeID()) {
            try {
                g.drawImage(ImageIO.read(new File(getClass().getClassLoader().getResource("images/p1.jpg").toURI())), (int) x, (int) y, (int) width, (int) width, null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (ObjectType.SPEED_UP.getObjectTypeID() == type.getObjectTypeID()) {
            try {
                g.drawImage(ImageIO.read(new File(getClass().getClassLoader().getResource("images/p0.jpg").toURI())), (int) x, (int) y, (int) width, (int) width, null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (ObjectType.JOIN_ALL.getObjectTypeID() == type.getObjectTypeID()) {
            try {
                g.drawImage(ImageIO.read(new File(getClass().getClassLoader().getResource("images/p3.jpg").toURI())), (int) x, (int) y, (int) width, (int) width, null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (ObjectType.HALFENER.getObjectTypeID() == type.getObjectTypeID()) {
            try {
                g.drawImage(ImageIO.read(new File(getClass().getClassLoader().getResource("images/p2.jpg").toURI())), (int) x, (int) y, (int) width, (int) width, null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (ObjectType.DESTROYER.getObjectTypeID() == type.getObjectTypeID()) {
            try {
                g.drawImage(ImageIO.read(new File(getClass().getClassLoader().getResource("images/p4.jpg").toURI())), (int) x, (int) y, (int) width, (int) width, null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
