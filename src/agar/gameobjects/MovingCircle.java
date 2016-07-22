package agar.gameobjects;

import agar.Game;
import agar.Spawner;

import java.awt.*;
import java.io.Serializable;


public class MovingCircle extends Circle implements Serializable {

    private Spawner spawner;
    private boolean isEaten;

    private boolean isGod, isSpeed;

    private int playerID;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public MovingCircle(double x, double y, double radius, ObjectType type, Color color, Spawner spawner, int playerID) {
        super(x, y, radius, type, color);
        this.spawner = spawner;
        this.playerID = playerID;

    }

    @Override
    public boolean update() {
        if (x >= Game.width - 5 || x <= 5) {
            velX *= -1;
        }
        if (y >= Game.height - 5 || y <= 5) {
            velY *= -1;
        }
        if (isSpeed) {
            x += (velX * 2 / getSurface());
            y += (velY * 2 / getSurface());
        } else {
            x += (velX / getSurface());
            y += (velY / getSurface());
        }
        eatFood();
        if (!isGod) {
            if (Game.checkforCollision(this)) {
                return false;
            }
            if (flag) return false;
        }

        return true;
    }

    private void eatFood() {
        for (int i = 0; i < spawner.getFoods().size(); i++) {
            if (spawner.getFoods().get(i).getBounds().intersects(this.getBounds())) {
                this.surface += spawner.getFoods().get(i).getSurface();
                setRadiusfromSurface();
                width = radius * 2;
                spawner.getFoods().get(i).setEaten(true);
            }
        }
    }


    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }


    public Spawner getSpawner() {
        return spawner;
    }

    public void setSpawner(Spawner spawner) {
        this.spawner = spawner;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public boolean isSpeed() {
        return isSpeed;
    }

    public void setSpeed(boolean speed) {
        isSpeed = speed;
    }

    public boolean isGod() {
        return isGod;
    }

    public void setGod(boolean god) {
        isGod = god;
    }

}
