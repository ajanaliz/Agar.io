package agar.gameobjects;

import java.awt.*;
import java.io.Serializable;


public class Food extends GameObject implements Serializable {

    private boolean isEaten;

    public Food(double x, double y, ObjectType type, Color color) {
        super(x, y, type, color);
        isEaten = false;
        if (type == ObjectType.ORDINARY_FOOD) {
            this.radius = Math.random() * 20;
            width = (radius * 2);
            this.surface = radius * radius * Math.PI;
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) width, (int) width);
    }

    @Override
    public boolean update() {
        if (isEaten)
            return false;
        return true;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int) (x), (int) (y), (int) width, (int) width);
        g.setColor(color.darker());
        g.setStroke(new BasicStroke(6));
        g.drawOval((int) (x), (int) (y), (int) width, (int) width);
        g.setStroke(new BasicStroke(1));
    }


    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }
}
