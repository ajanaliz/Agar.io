package agar.gameobjects;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Serializable{

    protected double x;
    protected double y;
    protected double width;
    protected double radius;
    protected double surface;
    protected Color color;
    protected double velX, velY;//the speed in our X direction and the speed in our Y direction

    protected ObjectType type;

    public GameObject(double x, double y, ObjectType type, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.type = type;
        this.width = radius * 2;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) width, (int) width);
    }/*basically we're going to be using rectangles to handle all of our collision because within our java libraries,we have a Rectangle class(within the JRE)-->that has a method in it called
    ".intersect" which basically handles if two rectangles intersect each other it will return true*/

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setSurfacefromRadius(){
        surface = radius * radius * Math.PI;
    }

    public void setRadiusfromSurface(){
        radius = Math.sqrt(surface / Math.PI);

    }

    public double getY() {
        return y;
    }


    public void setY(double y) {
        this.y = y;
    }

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public abstract boolean update();

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public abstract void render(Graphics2D g);

    public double distanceTo(double dx, double dy) {
        double diffx = dx - x;
        diffx *= diffx;
        double diffy = dy - y;
        diffy *= diffy;
        return Math.sqrt(diffx + diffy);
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }
}
