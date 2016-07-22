package agar.gameobjects;

import agar.Game;

import java.awt.*;
import java.io.Serializable;

public class Circle extends GameObject implements Serializable {


    public static final int MINIMUM_RADIUS = 10;

    public Circle(double x, double y, double radius, ObjectType type, Color color) {
        super(x, y, type, color);
        this.radius = radius;
        this.width = radius * 2;
        this.surface = getSurface();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x,(int) y,(int) width,(int) width);
    }

    @Override
    public boolean update() {
        if (x >= Game.width - 5 || x <= 5) {
            velX *= -1;
        }
        if (y >= Game.height - 5 || y <= 5) {
            velY *= -1;
        }
        x += (velX / getSurface());
        y += (velY / getSurface());
        return true;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int)(x),(int)(y), (int) width, (int) width);
        g.setColor(color.darker());
        g.setStroke(new BasicStroke(6));
        g.drawOval((int)(x),(int)(y),(int) width, (int) width);
        g.setStroke(new BasicStroke(1));
    }


    public double getSurface() {
        return (radius * radius * Math.PI);
    }
}
