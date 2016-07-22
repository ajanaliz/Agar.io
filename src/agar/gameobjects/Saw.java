package agar.gameobjects;

import agar.MainMenu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

public class Saw extends  Food implements Serializable{

    private int scale;
    public Saw(double x, double y, ObjectType type, Color color) {
        super(x, y, type, color);
        scale = MainMenu.gearSizeFactor;
        radius = 15 * scale;
        width = radius * 2;
    }


    @Override
    public double getSurface() {
        return (radius*radius*Math.PI);
    }

    @Override
    public boolean update() {
        return true;
    }

    @Override
    public boolean isEaten() {
        return super.isEaten();
    }

    @Override
    public void render(Graphics2D g) {
        try {
            g.drawImage(ImageIO.read(new File(getClass().getClassLoader().getResource("images/gear.png").toURI())),(int)x,(int)y,(int)width,(int)width,null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
